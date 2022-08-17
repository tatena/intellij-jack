package ge.freeuni.jack.language.psi

import com.intellij.psi.stubs.StubElement

interface JackStubElement<T: JackElement>: JackElement, StubElement<T> {
}