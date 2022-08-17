package ge.freeuni.jack.language.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.psi.stubs.IStubElementType
import ge.freeuni.jack.language.psi.JackStubElementBase
import ge.freeuni.jack.language.psi.JackNamedElement
import ge.freeuni.jack.language.psi.JackStubElement

abstract class JackNamedStubElementBase<T : JackStubElement<*>> 
    : JackStubElementBase<T>, JackNamedElement 
{
    constructor(stub: T, nodeType: IStubElementType<*, *>) : super(stub, nodeType)
    constructor(node: ASTNode) : super(node)
}
