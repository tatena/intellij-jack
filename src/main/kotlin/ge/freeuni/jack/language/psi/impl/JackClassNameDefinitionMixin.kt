package ge.freeuni.jack.language.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.openapi.util.text.StringUtil
import com.intellij.psi.PsiElement
import com.intellij.psi.stubs.IStubElementType
import ge.freeuni.jack.language.psi.JackClassNameDefinition
import ge.freeuni.jack.language.psi.util.JackElementFactory
import ge.freeuni.jack.language.stub.JackNamedStubElementBase
import ge.freeuni.jack.language.stub.impl.JackClassNameDefStub

abstract class 
JackClassNameDefinitionMixin: JackNamedStubElementBase<JackClassNameDefStub>, JackClassNameDefinition {
    constructor(stub: JackClassNameDefStub, nodeType: IStubElementType<*, *>) : super(stub, nodeType)
    constructor(node: ASTNode) : super(node)

    override fun getName(): String? {
        val stub = stub
        return if (stub != null) StringUtil.notNullize(stub.name) else this.nameIdentifier!!.text
    }

    override fun getNameIdentifier(): PsiElement? {
        return this.node.psi
    }

    override fun setName(name: String): PsiElement {
        val elem = JackElementFactory.createClassNameDef(project, name)
        replace(elem)
        return this
    }
}