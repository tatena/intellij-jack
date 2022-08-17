package ge.freeuni.jack.language.psi

import com.intellij.extapi.psi.StubBasedPsiElementBase
import com.intellij.lang.ASTNode
import com.intellij.psi.stubs.IStubElementType

abstract class JackStubElementBase<T : JackStubElement<*>> : StubBasedPsiElementBase<T> {
    constructor(stub: T, nodeType: IStubElementType<*, *>) : super(stub, nodeType)
    constructor(node: ASTNode) : super(node)

    override fun toString(): String {
        return elementType.toString()
    }
}
