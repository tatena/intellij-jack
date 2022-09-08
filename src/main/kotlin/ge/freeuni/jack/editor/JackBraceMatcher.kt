package ge.freeuni.jack.editor

import com.intellij.lang.BracePair
import com.intellij.lang.PairedBraceMatcher
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IElementType
import ge.freeuni.jack.language.psi.JackTypes

class JackBraceMatcher: PairedBraceMatcher {
    override fun getPairs(): Array<BracePair> = arrayOf(
        BracePair(JackTypes.LBRACE, JackTypes.RBRACE, true),
        BracePair(JackTypes.LPAREN, JackTypes.RPAREN, false),
        BracePair(JackTypes.LBRACK, JackTypes.RBRACK, false)
    )

    override fun isPairedBracesAllowedBeforeType(lbraceType: IElementType, contextType: IElementType?): Boolean = true

    override fun getCodeConstructStart(file: PsiFile?, openingBraceOffset: Int): Int = openingBraceOffset

}