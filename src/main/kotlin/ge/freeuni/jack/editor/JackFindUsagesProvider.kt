package ge.freeuni.jack.editor

import com.intellij.lang.HelpID
import com.intellij.lang.cacheBuilder.DefaultWordsScanner
import com.intellij.lang.cacheBuilder.WordsScanner
import com.intellij.lang.findUsages.FindUsagesProvider
import com.intellij.psi.ElementDescriptionUtil
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.TokenSet
import com.intellij.usageView.UsageViewLongNameLocation
import com.intellij.usageView.UsageViewShortNameLocation
import ge.freeuni.jack.language.JackLexerAdapter
import ge.freeuni.jack.language.psi.JackClassNameDefinition
import ge.freeuni.jack.language.psi.JackNamedElement
import ge.freeuni.jack.language.psi.JackReferenceType
import ge.freeuni.jack.language.psi.JackTypes

class JackFindUsagesProvider: FindUsagesProvider {
    override fun getWordsScanner(): WordsScanner? {
        return DefaultWordsScanner(
            JackLexerAdapter(),
            TokenSet.create(JackTypes.IDENTIFIER, JackTypes.CLASS_NAME_DEFINITION, JackTypes.REFERENCE_TYPE),
            TokenSet.EMPTY,
            TokenSet.EMPTY
        )
    }
    
    override fun canFindUsagesFor(psiElement: PsiElement): Boolean {
        return psiElement is JackNamedElement
    }

    override fun getHelpId(psiElement: PsiElement): String? {
        return HelpID.FIND_OTHER_USAGES
    }

    override fun getType(element: PsiElement): String {
        return when (element) {
            is JackClassNameDefinition -> "class_name"
            is JackReferenceType -> "class_ref"
            else -> "unknown"
        }
    }

    override fun getDescriptiveName(element: PsiElement): String {
        return ElementDescriptionUtil.getElementDescription(element, UsageViewLongNameLocation.INSTANCE)
    }

    override fun getNodeText(element: PsiElement, useFullName: Boolean): String {
        return ElementDescriptionUtil.getElementDescription(element, UsageViewShortNameLocation.INSTANCE)
    }
}