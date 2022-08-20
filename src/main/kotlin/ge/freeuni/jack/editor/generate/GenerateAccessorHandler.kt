package ge.freeuni.jack.editor.generate

import com.intellij.codeInsight.CodeInsightActionHandler
import com.intellij.codeInsight.actions.CodeInsightAction
import com.intellij.codeInsight.generation.ClassMember
import com.intellij.codeInsight.generation.MemberChooserObject
import com.intellij.codeInsight.generation.MemberChooserObjectBase
import com.intellij.ide.util.MemberChooser
import com.intellij.lang.LanguageCodeInsightActionHandler
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.runWriteAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.ui.components.textFieldWithBrowseButton
import ge.freeuni.jack.language.JackIcons
import ge.freeuni.jack.language.JackUtil
import ge.freeuni.jack.language.psi.JackClassDeclaration
import ge.freeuni.jack.language.psi.JackFunc
import ge.freeuni.jack.language.psi.JackProperty
import ge.freeuni.jack.language.psi.util.JackElementFactory
import io.perfmark.Impl
import org.jetbrains.uast.test.common.allUElementSubtypes


class GenerateGetterAction : CodeInsightAction() {

    val handler: GenerateGetterHandler = GenerateGetterHandler()

    override fun getHandler(): CodeInsightActionHandler = handler

}

class GenerateGetterHandler : LanguageCodeInsightActionHandler {
    override fun invoke(project: Project, editor: Editor, file: PsiFile) {
//        check(!ApplicationManager.getApplication().isWriteAccessAllowed)
        val optProps = PsiTreeUtil.findChildOfType(file, JackClassDeclaration::class.java)

        var selected = arrayListOf<JackMemberChooser>()
        optProps?.let { def ->
            val props = def.classBody?.propertyList
            val base = MemberChooserObjectBase(def.className)
            if (props != null) {
                val res = arrayListOf<JackMemberChooser>()
                for (prop in props) {
                    res.addAll(toChooser(base, prop))
                }
                val chooser = MemberChooser(res.toTypedArray(), true, true, project).apply { 
                    title = "Getters"
                    selectElements(res.toTypedArray())
                    setCopyJavadocVisible(false)
                }
                if (res.isNotEmpty()) {
                    chooser.show()
                    chooser.selectedElements?.let { selected.addAll(it) }
                }
            }
            runWriteAction {
                selected.forEach { member -> 
                    val text = "\nmethod void get_${member.text}() {\n\treturn this.${member.text};\n}\n"
                    val method: JackFunc = JackElementFactory.createMethod(text, project)
                    def.classBody?.addBefore(method, def.classBody?.rbrace)
                }
            }
        }

    }

    private fun toChooser(base: MemberChooserObjectBase, prop: JackProperty?): List<JackMemberChooser> {
        val res = arrayListOf<JackMemberChooser>()
        prop?.propertyDefinitionList?.forEach { def ->
            res.add(JackMemberChooser(base, def.identifier.text))
        }
        return res
    }

    override fun isValidFor(editor: Editor?, file: PsiFile?): Boolean = true

}


class JackMemberChooser(
    private val base: MemberChooserObjectBase,
    text: String
) : MemberChooserObjectBase(text, JackIcons.FILE), ClassMember 
{
    override fun getParentNodeDelegate(): MemberChooserObject = base
}