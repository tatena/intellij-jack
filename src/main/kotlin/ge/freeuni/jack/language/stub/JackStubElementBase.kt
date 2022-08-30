package ge.freeuni.jack.language.stub

import com.intellij.extapi.psi.StubBasedPsiElementBase
import com.intellij.lang.ASTNode
import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.stubs.StubElement
import ge.freeuni.jack.language.psi.JackElement

abstract class JackStubElementBase<T : StubElement<*>> : StubBasedPsiElementBase<T>, JackElement {
    constructor(stub: T, nodeType: IStubElementType<*, *>) : super(stub, nodeType)
    constructor(node: ASTNode) : super(node)

    override fun toString(): String {
        return elementType.toString()
    }
}
