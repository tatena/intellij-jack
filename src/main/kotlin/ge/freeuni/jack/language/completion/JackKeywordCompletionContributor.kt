package ge.freeuni.jack.language.completion

import com.fasterxml.jackson.databind.JavaType
import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.patterns.ElementPattern
import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.patterns.PsiElementPattern
import com.intellij.psi.PsiElement
import com.intellij.psi.TokenType
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

    private fun extendBasic(
        pattern: PsiElementPattern.Capture<PsiElement>,
        vararg keywords: String
    ) {
        extend(CompletionType.BASIC, pattern, JackKeywordCompletionProvider(keywords = listOf(*keywords)))
    }

    init {
//        registerStandardCompletion(propertyPattern(), false, FIELD, STATIC)
//        registerStandardCompletion(propertyTypePattern(), false, INT, CHAR, BOOLEAN)

        extendBasic(newPropPattern(), "field", "static", "function", "constructor", "methods")
        extendBasic(newFuncPattern(), "function", "constructor", "methods")

        extendBasic(newStmtPattern(), "var", "let", "if", "do", "while", "return")

        extendBasic(newTypePattern(), "int", "boolean", "char")
        
//        extend(CompletionType.BASIC, dotPattern(), JackThisCompletionProvider())
    }

    private fun newTypePattern(): PsiElementPattern.Capture<PsiElement> {
        return psiElement(JackTypes.IDENTIFIER).withParent(
            psiElement(JackTypes.REFERENCE_TYPE)
        )
    }

    private fun newStmtPattern(): PsiElementPattern.Capture<PsiElement> {
        return psiElement(JackTypes.IDENTIFIER).withParent(
            psiElement(JackTypes.STATEMENT_PLACEHOLDER).withSuperParent(2,
                psiElement().andOr(
                    psiElement(JackTypes.FUNC_BODY),
                    psiElement(JackTypes.IF_BODY),
                    psiElement(JackTypes.ELSE_BODY),
                    psiElement(JackTypes.WHILE_BODY)
                )
            )
        )
    }

    private fun newFuncPattern(): PsiElementPattern.Capture<PsiElement> {
        return psiElement(JackTypes.IDENTIFIER).withParent(
            psiElement(JackTypes.FUNC_PLACEHOLDER).withParent(
                psiElement(JackTypes.CLASS_BODY)
            )
        )
    }

    private fun newPropPattern(): PsiElementPattern.Capture<PsiElement> {
        return psiElement(JackTypes.IDENTIFIER).withParent(
            psiElement(JackTypes.PROPERTY_PLACEHOLDER).withParent(
                psiElement(JackTypes.CLASS_BODY)
            )
        )
    }

    private fun dotPattern(): PsiElementPattern.Capture<PsiElement> {
        return psiElement().withParent(psiElement(JackTypes.VAR_REFERENCE).afterLeaf(psiElement(JackTypes.DOT)))
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