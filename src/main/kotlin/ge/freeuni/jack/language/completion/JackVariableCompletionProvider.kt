package ge.freeuni.jack.language.completion

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
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
                    .withCaseSensitivity(false)
                    .withTypeText(item.type)
                    .withIcon(JackIcons.FIELD)
            )
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