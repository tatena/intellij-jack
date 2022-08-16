package ge.freeuni.jack.language.psi

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider
import ge.freeuni.jack.language.JackFileType
import ge.freeuni.jack.language.JackLanguage

class JackFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, JackLanguage.INSTANCE)  {
    override fun getFileType(): FileType {
        return JackFileType.INSTANCE
    }

    override fun toString(): String {
        return "JackFile"
    }
}