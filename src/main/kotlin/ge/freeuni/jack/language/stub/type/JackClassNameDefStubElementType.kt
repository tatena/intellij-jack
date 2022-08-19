package ge.freeuni.jack.language.stub.type

import com.intellij.lang.ASTNode
import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import ge.freeuni.jack.language.psi.JackClassDeclaration
import ge.freeuni.jack.language.psi.JackClassNameDefinition
import ge.freeuni.jack.language.psi.impl.JackClassNameDefinitionImpl
import ge.freeuni.jack.language.stub.impl.JackClassNameDefStub

class JackClassNameDefStubElementType(debugName: String): JackNamedStubElementType<JackClassNameDefStub, JackClassNameDefinition>(debugName) {
    override fun serialize(stub: JackClassNameDefStub, dataStream: StubOutputStream) {
        dataStream.writeName(stub.name)
    }

    override fun shouldCreateStub(node: ASTNode?): Boolean {
        val psi = node?.psi
        if (psi !is JackClassNameDefinition) {
            return false
        }
        return psi.parent is JackClassDeclaration
    }

    override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): JackClassNameDefStub {
        return JackClassNameDefStub(parentStub, this, dataStream.readName())
    }

    override fun createPsi(stub: JackClassNameDefStub): JackClassNameDefinition {
        return JackClassNameDefinitionImpl(stub, this)
    }

    override fun createStub(psi: JackClassNameDefinition, parentStub: StubElement<*>?): JackClassNameDefStub {
        return JackClassNameDefStub(parentStub, this, psi.name)
    }
}