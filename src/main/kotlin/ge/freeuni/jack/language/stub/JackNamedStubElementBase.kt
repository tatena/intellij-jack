package ge.freeuni.jack.language.stub

import com.intellij.lang.ASTNode
import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.stubs.StubElement
import ge.freeuni.jack.language.psi.JackNamedElement

abstract class JackNamedStubElementBase<T : StubElement<*>> 
    : JackStubElementBase<T>, JackNamedElement 
{
    constructor(stub: T, nodeType: IStubElementType<*, *>) : super(stub, nodeType)
    constructor(node: ASTNode) : super(node)
}
