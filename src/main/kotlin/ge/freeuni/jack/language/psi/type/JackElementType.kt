package ge.freeuni.jack.language.psi.type

import com.intellij.psi.tree.IElementType
import ge.freeuni.jack.language.JackLanguage

class JackElementType(
    debugName: String
) : IElementType(debugName, JackLanguage.INSTANCE)  {
}