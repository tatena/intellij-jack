package ge.freeuni.jack.editor.generate

import com.intellij.codeInsight.CodeInsightActionHandler
import com.intellij.codeInsight.actions.CodeInsightAction
import com.intellij.codeInsight.generation.MemberChooserObjectBase
import com.intellij.ide.util.MemberChooser
import com.intellij.lang.LanguageCodeInsightActionHandler
import com.intellij.openapi.application.WriteActionAware
import com.intellij.openapi.application.runWriteAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiParserFacade
import com.intellij.psi.util.PsiTreeUtil
import ge.freeuni.jack.language.JackIcons
import ge.freeuni.jack.language.psi.JackClassDeclaration
import ge.freeuni.jack.language.psi.JackProperty
import ge.freeuni.jack.language.psi.util.JackElementFactory

class GenerateConstructorAction: CodeInsightAction() {
    private val handler = GenerateConstructorHandler()

    override fun getHandler(): CodeInsightActionHandler = handler
}



class GenerateConstructorHandler: LanguageCodeInsightActionHandler, DumbAware, WriteActionAware {
    private fun toPropItems(base: MemberChooserObjectBase, prop: JackProperty): List<JackPropertyChooser> {
        val type = prop.type

        val scope = prop.propertyScope.scope
        
        if (scope == "function") {
            return listOf()
        }

        val choosers = arrayListOf<JackPropertyChooser>()
        prop.propertyDefinitionList.forEach { def ->
            val name = def.identifier.text
            val qualifiedName = name.replaceFirstChar { it.uppercase() }
            choosers.add(JackPropertyChooser(type, scope, base, name, qualifiedName))
        }

        return choosers
    }
    
    
    override fun invoke(project: Project, editor: Editor, file: PsiFile) {
        val jclass = PsiTreeUtil.findChildOfType(file, JackClassDeclaration::class.java) ?: return

        val body = jclass.classBody ?: return
        val properties = body.propertyList
        val base = MemberChooserObjectBase(jclass.className, JackIcons.FILE)

        val members = arrayListOf<JackPropertyChooser>()

        for (prop in properties) {
            members.addAll(this.toPropItems(base, prop))
        }


        if (members.isEmpty()) {
            return
        }

        val chooser = MemberChooser(members.toTypedArray(), true, true, project)
            .apply {
                title = "Getters"
                selectElements(members.toTypedArray())
                setCopyJavadocVisible(false)
            }

        val selected = arrayListOf<JackPropertyChooser>()

        chooser.show()
        chooser.selectedElements?.let { selected.addAll(it) }
        
        val classname = jclass.classNameDefinition?.identifier?.text ?: return
        var args = ""
        var assigns = ""
        val methodTexts = arrayListOf<String>()
        selected.forEach { member ->
            args += "${member.type} ${member.name}, "
            assigns += "\tlet this.${member.name} = ${member.name};\n"
        }
        if (args.length > 1) {
            args = args.dropLast(2)
        }
        methodTexts.add(
            "constructor $classname new$classname($args) {\n" +
            assigns +
            "\treturn this;\n" +
            "}"
        )
        runWriteAction {
            val methods = JackElementFactory.createMethods(methodTexts, project)
            val newline = PsiParserFacade.SERVICE.getInstance(project)
                .createWhiteSpaceFromText("\n\n");
            body.addBefore(newline, body.rbrace)

            for (method in methods) {
                body.addBefore(method, body.rbrace)
                body.addBefore(newline, body.rbrace)
            }
        }
    }

    override fun isValidFor(editor: Editor?, file: PsiFile?): Boolean = true
}