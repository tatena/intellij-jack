package ge.freeuni.jack.language.psi

import com.intellij.psi.tree.IElementType
import ge.freeuni.jack.language.JackLanguage

class JackTokenType(
    debugName: String
) : IElementType(debugName, JackLanguage.INSTANCE) {

    override fun toString(): String {
        return "JackTokenType." + super.toString()
    }
}