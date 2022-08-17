package ge.freeuni.jack.language

import com.intellij.openapi.fileTypes.LanguageFileType
import javax.swing.Icon

class JackFileType:  LanguageFileType(JackLanguage.INSTANCE) {
    
    override fun getName(): String {
        return "Jack File"
    }

    override fun getDescription(): String {
        return "Jack language file"
    }

    override fun getDefaultExtension(): String {
        return "jack"
    }

    override fun getIcon(): Icon {
        return JackIcons.FILE
    }

    companion object {
        @JvmStatic
        val INSTANCE = JackFileType()
    }
}