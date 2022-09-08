package ge.freeuni.jack.language.completion

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.editor.EditorModificationUtil
import com.intellij.psi.util.elementType
import com.intellij.util.ProcessingContext
import ge.freeuni.jack.language.JackIcons
import ge.freeuni.jack.language.JackUtil
import ge.freeuni.jack.language.psi.JackTypes
import ge.freeuni.jack.language.psi.JackVarReference

class JackVariableCompletionProvider : CompletionProvider<CompletionParameters>() {
    override fun addCompletions(
        parameters: CompletionParameters,
        context: ProcessingContext,
        result: CompletionResultSet
    ) {
        val elem = parameters.position

        if (elem.parent.elementType != JackTypes.VAR_REFERENCE) {
            return
        }
        val names: Set<PropertyItem> = JackUtil.getAllVariables(elem.parent as JackVarReference)
        names.forEach { item ->
            result.addElement(
                LookupElementBuilder
                    .create(item.name)
                    .withCaseSensitivity(true)
                    .withTypeText(item.type)
                    .withIcon(JackIcons.FIELD)
            )
        }
        val methods = JackUtil.findMethods(elem.parent as JackVarReference)
        methods.forEach { func ->
            var paramText = "("
            func.funcParams?.paramList?.forEach { param ->
                paramText += "${param.type} ${param.propertyDefinition?.identifier?.text}, "
            }
            if (paramText.length > 1) {
                paramText = paramText.dropLast(2)
            }
            paramText += ")"
            func.funcNameDefinition?.let { name ->
                result.addElement(
                    LookupElementBuilder.create(name.identifier.text)
                        .withCaseSensitivity(true)
                        .withIcon(JackIcons.METHOD)
                        .withTypeText(getFuncType(func))
                        .withTailText(paramText)
                        .withInsertHandler { ctx, _ -> 
                            ctx.document.insertString(
                                ctx.selectionEndOffset, "()"
                            )
                            EditorModificationUtil.moveCaretRelatively(ctx.editor, 1)
                        }
                )
            }
        }
    }

}
enum class PropertyScope {
    LOCAL, PARAM, FIELD, STATIC;

    companion object {
        @JvmStatic
        fun resolveScope(scope: String): PropertyScope {
            if (scope == "method")
                return FIELD
            
            return STATIC
        }
    }
}

data class PropertyItem(val name: String, val type: String, val scope: PropertyScope) {

    override fun equals(other: Any?): Boolean {
        if (other === null) return false
        if (other !is PropertyItem) return false
        
        return other.name == this.name
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }
} 