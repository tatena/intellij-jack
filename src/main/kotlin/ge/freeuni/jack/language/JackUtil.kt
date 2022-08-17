package ge.freeuni.jack.language

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiManager
import com.intellij.psi.search.FileTypeIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.PsiTreeUtil
import ge.freeuni.jack.language.psi.JackClassDeclaration
import ge.freeuni.jack.language.psi.JackFile

object JackUtil {
    @JvmStatic
    fun findClasses(project: Project): List<JackClassDeclaration> {
        val res = arrayListOf<JackClassDeclaration>()
        val vFiles = FileTypeIndex.getFiles(JackFileType.INSTANCE, GlobalSearchScope.allScope(project))
        for (virtualFile in vFiles) {
            val file = PsiManager.getInstance(project).findFile(virtualFile) as? JackFile
            if (file != null) {
                val jackClass = PsiTreeUtil.getChildOfType(file, JackClassDeclaration::class.java)
                jackClass?.let { res.add(it) }
            }
        }
        return res
    }
    
    @JvmStatic
    fun findClass(project: Project, name: String): JackClassDeclaration? {
        
        val vFiles = FileTypeIndex.getFiles(JackFileType.INSTANCE, GlobalSearchScope.allScope(project))
        for (virtualFile in vFiles) {
            val file = PsiManager.getInstance(project).findFile(virtualFile) as? JackFile
            if (file != null) {
                val jackClass = PsiTreeUtil.getChildOfType(file, JackClassDeclaration::class.java)
                jackClass?.let { jclass ->
                    println("trying to match: ${jclass.className ?: "COULDNT GET NAME"}\n" +
                            "with name: $name")
                    if (jclass.className.toString() == name) {
                        return jclass
                    }
                }
            }
        }
        return null
    }
}