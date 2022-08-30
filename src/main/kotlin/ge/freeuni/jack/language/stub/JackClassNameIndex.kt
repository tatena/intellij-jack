package ge.freeuni.jack.language.stub

import com.intellij.psi.stubs.StringStubIndexExtension
import com.intellij.psi.stubs.StubIndexKey
import ge.freeuni.jack.language.psi.JackNamedElement

class JackClassNameIndex: StringStubIndexExtension<JackNamedElement>() {
    override fun getVersion(): Int {
        return super.getVersion() + VERSION
    }

    override fun getKey(): StubIndexKey<String, JackNamedElement> {
        return KEY
    }

    companion object {
        val KEY: StubIndexKey<String, JackNamedElement> =
            StubIndexKey.createIndexKey("jack.class.name")
        const val VERSION = 0
    }
}
