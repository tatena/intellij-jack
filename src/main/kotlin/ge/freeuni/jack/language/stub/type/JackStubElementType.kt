package ge.freeuni.jack.language.stub.type

import com.intellij.psi.stubs.IStubElementType
import com.intellij.psi.stubs.StubElement
import ge.freeuni.jack.language.JackLanguage
import ge.freeuni.jack.language.psi.JackElement

abstract class JackStubElementType<S, T>(debugName: String): 
        IStubElementType<S, T>(debugName, JackLanguage.INSTANCE) 
        where T: JackElement, S: StubElement<T>
{
    override fun getExternalId(): String {
        return "jack.${super.toString()}"
    }
}