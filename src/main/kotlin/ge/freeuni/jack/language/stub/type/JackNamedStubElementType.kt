package ge.freeuni.jack.language.stub.type

import com.intellij.psi.stubs.IndexSink
import com.intellij.psi.stubs.NamedStubBase
import ge.freeuni.jack.language.psi.JackNamedElement
import ge.freeuni.jack.language.stub.JackClassNameIndex

abstract class JackNamedStubElementType<S, T>(debugName: String) : JackStubElementType<S, T>(debugName)
        where T : JackNamedElement, S : NamedStubBase<T> 
{
    override fun indexStub(stub: S, sink: IndexSink) {
        val name = stub.name
        if (name != null) {
            sink.occurrence(JackClassNameIndex.KEY, name)
        }
    }
}