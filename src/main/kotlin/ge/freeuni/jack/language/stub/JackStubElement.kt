package ge.freeuni.jack.language.stub

import com.intellij.psi.stubs.StubElement
import ge.freeuni.jack.language.psi.JackElement

interface JackStubElement<T: JackElement>: JackElement, StubElement<T> {
}