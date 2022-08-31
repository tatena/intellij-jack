package ge.freeuni.jack.language.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.openapi.util.text.StringUtil
import com.intellij.psi.PsiElement
import com.intellij.psi.stubs.IStubElementType
import ge.freeuni.jack.language.psi.JackFuncNameDefinition
import ge.freeuni.jack.language.psi.JackPropertyDefinition
import ge.freeuni.jack.language.psi.util.JackElementFactory
import ge.freeuni.jack.language.stub.JackNamedStubElementBase
import ge.freeuni.jack.language.stub.impl.JackMethodDefinitionStub
import ge.freeuni.jack.language.stub.impl.JackPropertyDefinitionStub

abstract class JackMethodDefinitionMixin: JackNamedStubElementBase<JackMethodDefinitionStub>, JackFuncNameDefinition {
    
    constructor(stub: JackMethodDefinitionStub, nodeType: IStubElementType<*, *>) : super(stub, nodeType)
    constructor(node: ASTNode) : super(node)

    override fun setName(name: String): PsiElement {
        val elem = JackElementFactory.createFunctionDefinition(project, name)
        replace(elem)
        return this
    }

    override fun getNameIdentifier(): PsiElement? {
        return this.node.psi
    }

    override fun getName(): String? {
        val stub = stub
        return if (stub != null) StringUtil.notNullize(stub.name) else this.nameIdentifier!!.text
    }
}