package ge.freeuni.jack.language.completion

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.ProcessingContext
import ge.freeuni.jack.language.JackIcons
import ge.freeuni.jack.language.psi.JackClassDeclaration

class JackThisCompletionProvider : CompletionProvider<CompletionParameters>() {
    override fun addCompletions(
        parameters: CompletionParameters,
        context: ProcessingContext,
        result: CompletionResultSet
    ) {
        
        val jclass = PsiTreeUtil.findChildOfType(parameters.originalFile, JackClassDeclaration::class.java)
        if (jclass != null) {
            jclass.classBody?.propertyList?.forEach { item -> 
                item.propertyDefinitionList.forEach { def ->
                    result.addElement(
                        LookupElementBuilder
                            .create(def.identifier.text)
                            .withCaseSensitivity(false)
                            .withTypeText(item.type)
                            .withIcon(JackIcons.FILE)
                    )
                }
            }
            
        }
        val lookup = LookupElementBuilder.create("BaroDZmao").withCaseSensitivity(false)
        result.addElement(lookup)
    }

}
