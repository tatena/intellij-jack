package ge.freeuni.jack.language.lib

import com.intellij.navigation.ItemPresentation
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.AdditionalLibraryRootsProvider
import com.intellij.openapi.roots.SyntheticLibrary
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.impl.FakePsiElement
import com.intellij.psi.impl.PsiFileFactoryImpl
import com.intellij.psi.impl.file.impl.FileManager
import com.intellij.psi.impl.file.impl.FileManagerImpl
import ge.freeuni.jack.language.JackFileType
import ge.freeuni.jack.language.JackIcons
import ge.freeuni.jack.language.JackUtil
import ge.freeuni.jack.language.psi.JackFile
import ge.freeuni.jack.language.psi.util.JackElementFactory
import javax.swing.Icon

class JackLibraryProvider: AdditionalLibraryRootsProvider() {

    override fun getAdditionalProjectLibraries(project: Project): MutableCollection<SyntheticLibrary> {
        val vf = ArrayVF(project)
        
        return arrayListOf(JackSyntheticLibrary(hashSetOf()))
    }
}



class JackSyntheticLibrary(private val sourceRoots: Set<VirtualFile>): SyntheticLibrary(), ItemPresentation {
    override fun equals(other: Any?): Boolean =
        other is JackSyntheticLibrary && other.sourceRoots == sourceRoots

    override fun hashCode(): Int = sourceRoots.hashCode()
    
    override fun getPresentableText(): String = "Jack STL"

    override fun getIcon(unused: Boolean): Icon = JackIcons.FILE

    override fun getSourceRoots(): Collection<VirtualFile> = sourceRoots

}



private fun ArrayVF(project: Project): VirtualFile? {
    val filename = "Array.jack"
    val classname = "Array"
    
    val jclass = 
        """
            class $classname { 
                constructor Array new(int size) {
                    return this;
                }
                
                method void dispose() {
                    return;
                }
            }
        """
    val i = PsiFileFactory.getInstance(project)
    val file = i.createFileFromText(filename, JackFileType.INSTANCE, jclass)
    
    if (file.virtualFile == null) {
        println("baro lashukaaaaaaaaaaa")
        return null
    }
//    return PsiFileFactory.getInstance(project)
//        .createFileFromText(filename, JackFileType.INSTANCE, jclass)
//        .virtualFile
    return null
}
