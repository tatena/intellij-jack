package ge.freeuni.jack.language.psi

import com.intellij.psi.NavigatablePsiElement
import com.intellij.psi.PsiNameIdentifierOwner

interface JackNamedElement: PsiNameIdentifierOwner, JackElement, NavigatablePsiElement {
}