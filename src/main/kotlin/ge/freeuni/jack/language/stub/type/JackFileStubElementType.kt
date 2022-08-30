package ge.freeuni.jack.language.stub.type

import com.intellij.psi.stubs.StubElement
import com.intellij.psi.stubs.StubInputStream
import com.intellij.psi.stubs.StubOutputStream
import com.intellij.psi.tree.IStubFileElementType
import ge.freeuni.jack.language.JackLanguage
import ge.freeuni.jack.language.stub.JackFileStub
import org.jetbrains.annotations.NonNls

class JackFileStubElementType(language: JackLanguage?): IStubFileElementType<JackFileStub>(language) {
    override fun getStubVersion(): Int = VERSION

    override fun serialize(stub: JackFileStub, dataStream: StubOutputStream) = Unit

    override fun deserialize(dataStream: StubInputStream, parentStub: StubElement<*>?): JackFileStub =
        JackFileStub(null)

    @NonNls
    override fun getExternalId(): String = super.getExternalId() + ".FILE"

    companion object {
        @JvmField
        val INSTANCE = JackFileStubElementType(JackLanguage.INSTANCE)

        private const val VERSION = 3 // Change the version if you want to re-index Frege
    }
}