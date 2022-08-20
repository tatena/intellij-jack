package ge.freeuni.jack.language.stub.type

import com.intellij.lang.ASTNode
import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import ge.freeuni.jack.language.psi.JackClassBody
import ge.freeuni.jack.language.psi.JackClassDeclaration
import ge.freeuni.jack.language.psi.JackClassNameDefinition
import ge.freeuni.jack.language.psi.JackPropertyDefinition
import ge.freeuni.jack.language.psi.impl.JackClassNameDefinitionImpl
import ge.freeuni.jack.language.psi.impl.JackPropertyDefinitionImpl
import ge.freeuni.jack.language.stub.impl.JackPropertyDefinitionStub

class JackPropertyDefinitionStubElementType(debugName: String)
    : JackNamedStubElementType<JackPropertyDefinitionStub, JackPropertyDefinition>(debugName)  {
    override fun serialize(stub: JackPropertyDefinitionStub, dataStream: StubOutputStream) {
        dataStream.writeName(stub.name)
    }

    override fun shouldCreateStub(node: ASTNode?): Boolean {
        val psi = node?.psi
        if (psi !is JackPropertyDefinition) {
            return false
        }
        return psi.parent is JackClassBody
    }

    override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): JackPropertyDefinitionStub {
        return JackPropertyDefinitionStub(parentStub, this, dataStream.readName())
    }

    override fun createPsi(stub: JackPropertyDefinitionStub): JackPropertyDefinition {
        return JackPropertyDefinitionImpl(stub, this)
    }

    override fun createStub(psi: JackPropertyDefinition, parentStub: StubElement<*>?): JackPropertyDefinitionStub {
        return JackPropertyDefinitionStub(parentStub, this, psi.name)
    }
}