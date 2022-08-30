package ge.freeuni.jack.language.stub

import com.intellij.psi.stubs.PsiFileStubImpl
import ge.freeuni.jack.language.psi.JackFile

class JackFileStub(file: JackFile?): PsiFileStubImpl<JackFile>(file)