package ge.freeuni.jack.language.completion

import com.fasterxml.jackson.databind.JavaType
import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.patterns.ElementPattern
import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.patterns.PsiElementPattern
import com.intellij.patterns.StandardPatterns.or
import com.intellij.psi.PsiElement
import com.intellij.psi.TokenType
import ge.freeuni.jack.language.psi.JackTypes

class JackKeywordCompletionContributor : CompletionContributor() {

    private fun extendBasic(
        pattern: PsiElementPattern.Capture<PsiElement>,
        vararg keywords: String
    ) {
        extend(CompletionType.BASIC, pattern, JackKeywordCompletionProvider(listOf(*keywords)))
    }

    init {
        extendBasic(newPropPattern(), "field", "static", "function", "constructor", "methods")
        extendBasic(newFuncPattern(), "function", "constructor", "methods")
        extendBasic(newStmtPattern(), "var", "let", "if", "do", "while", "return")
        extendBasic(newTypePattern(), "int", "boolean", "char")
        
        extend(CompletionType.BASIC, classRefPattern(), JackClassReferenceCompletionProvider())
        extend(CompletionType.BASIC, variablePattern(), JackVariableCompletionProvider())
        extend(CompletionType.BASIC, methodPattern(), JackMethodCompletionProvider())
    }

    private fun methodPattern(): PsiElementPattern.Capture<PsiElement> {
        return psiElement().afterLeaf(psiElement(JackTypes.DOT))
    }

    private fun variablePattern(): PsiElementPattern.Capture<PsiElement> {
        return psiElement(JackTypes.IDENTIFIER).withSuperParent(2,
            psiElement(JackTypes.REFERENCE_EXPR)
        )
    }

    private fun classRefPattern(): PsiElementPattern.Capture<PsiElement> {
        return psiElement(JackTypes.IDENTIFIER).withParent(
            or(
                psiElement(JackTypes.REFERENCE_TYPE),
                psiElement(JackTypes.VAR_REFERENCE)
                    .withParent(psiElement(JackTypes.REFERENCE_EXPR))
            )
        )
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
        super.fillCompletionVariants(parameters, result)
    }
}