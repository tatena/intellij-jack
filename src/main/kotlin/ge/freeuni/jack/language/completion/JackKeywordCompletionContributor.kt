package ge.freeuni.jack.language.completion

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.patterns.ElementPattern
import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.patterns.PsiElementPattern
import com.intellij.psi.PsiElement
import ge.freeuni.jack.language.psi.JackTypes

class JackKeywordCompletionContributor : CompletionContributor() {
    private fun registerStandardCompletion(
        pattern: ElementPattern<out PsiElement?>,
        needSpace: Boolean,
        vararg keywords: String
    ) {
        extend(
            CompletionType.BASIC,
            pattern,
            JackKeywordCompletionProvider(needSpace, listOf(*keywords))
        )
    }

    init {
        registerStandardCompletion(propertyPattern(), false, FIELD, STATIC)
        registerStandardCompletion(propertyTypePattern(), false, INT, CHAR, BOOLEAN)
    }


    override fun fillCompletionVariants(parameters: CompletionParameters, result: CompletionResultSet) {
        val bla = parameters.position.parent.firstChild
        super.fillCompletionVariants(parameters, result)
    }


    companion object {
        private const val FIELD = "field"
        private const val STATIC = "static"
        private const val INT = "int"
        private const val CHAR = "char"
        private const val BOOLEAN = "boolean"
        private const val SEMICOLON = ";"


        fun propertyPattern(): PsiElementPattern.Capture<PsiElement> {
            return  psiElement().andOr(
                psiElement().afterLeaf(psiElement(JackTypes.SEMICOLON)),
                psiElement().afterLeaf(psiElement(JackTypes.LBRACE))
            )
        }

        fun propertyTypePattern(): PsiElementPattern.Capture<PsiElement> {
            return psiElement().andOr(
                psiElement().afterLeaf(psiElement(JackTypes.FIELD)),
                psiElement().afterLeaf(psiElement(JackTypes.STATIC))
            )
        }

    }

}