package ge.freeuni.jack.language

import com.intellij.lang.Language

class JackLanguage: Language("Jack") {

    companion object {
        @JvmStatic
        val INSTANCE = JackLanguage()
    }
}