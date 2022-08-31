package ge.freeuni.jack.language.stub.type

import com.intellij.lang.ASTNode
import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import ge.freeuni.jack.language.psi.JackClassDeclaration
import ge.freeuni.jack.language.psi.JackClassNameDefinition
import ge.freeuni.jack.language.psi.JackFuncNameDefinition
import ge.freeuni.jack.language.psi.impl.JackClassNameDefinitionImpl
import ge.freeuni.jack.language.psi.impl.JackFuncNameDefinitionImpl
import ge.freeuni.jack.language.stub.impl.JackClassNameDefStub
import ge.freeuni.jack.language.stub.impl.JackMethodDefinitionStub

class JackMethodDefinitionStubElementType(debugName: String): JackNamedStubElementType<JackMethodDefinitionStub, JackFuncNameDefinition>(debugName) {
    override fun serialize(stub: JackMethodDefinitionStub, dataStream: StubOutputStream) {
        dataStream.writeName(stub.name)
    }

    override fun shouldCreateStub(node: ASTNode?): Boolean {
        val psi = node?.psi
        if (psi !is JackClassNameDefinition) {
            return false
        }
        return psi.parent is JackClassDeclaration
    }

    override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): JackMethodDefinitionStub {
        return JackMethodDefinitionStub(parentStub, this, dataStream.readName())
    }

    override fun createPsi(stub: JackMethodDefinitionStub): JackFuncNameDefinition {
        return JackFuncNameDefinitionImpl(stub, this)
    }

    override fun createStub(psi: JackFuncNameDefinition, parentStub: StubElement<*>?): JackMethodDefinitionStub {
        return JackMethodDefinitionStub(parentStub, this, psi.name)
    }
}