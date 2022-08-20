package ge.freeuni.jack.language

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.psi.search.FileTypeIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.PsiTreeUtil
import ge.freeuni.jack.language.psi.*

object JackUtil {
    @JvmStatic
    fun findClasses(project: Project): List<JackClassNameDefinition> {
        val res = arrayListOf<JackClassNameDefinition>()
        val vFiles = FileTypeIndex.getFiles(JackFileType.INSTANCE, GlobalSearchScope.allScope(project))
        for (virtualFile in vFiles) {
            val file = PsiManager.getInstance(project).findFile(virtualFile) as? JackFile
            if (file != null) {
                val jackClass = PsiTreeUtil.getChildOfType(file, JackClassDeclaration::class.java)
                jackClass?.let { jclass ->
                    val def = jclass.classNameDefinition
                    def?.let {
                        res.add(def)
                    }
                }
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
                    if (jclass.className.toString() == name) {
                        return jclass
                    }
                }
            }
        }
        return null
    }

    private fun getBody(elem: PsiElement): JackClassBody {
        var parent = elem.parent
        while (parent !is JackClassBody) {
            parent = parent.parent
        }
        return parent as JackClassBody
    }

    fun findClassProps(varRef: JackVarReference): List<JackProperty> {
        return getBody(varRef).propertyList
    }
//
//    fun findStatements(elem: JackVarReference): List<JackStatement> {
//        val funcs = getBody(elem).funcList
//        val res = arrayListOf<JackStmt>()
//        for (func in funcs) {
//            func.funcBody?.let { funcBody ->
//                res.addAll(funcBody.stmtList)
//            }
//        }
//        return res
//    }


    fun findLocalProps(elem: JackVarReference): List<JackLocalVars> {
        return getFuncBody(elem).localVarsList
    }

    private fun getFuncBody(elem: JackVarReference): JackFuncBody {
        var parent = elem.parent
        while (parent !is JackFuncBody) {
            parent = parent.parent
        }
        return parent as JackFuncBody
    }

    private fun getFuncParams(elem: JackVarReference): JackFuncParams? {
        var parent = elem.parent
        while (parent !is JackFunc) {
            parent = parent.parent
        }
        return (parent as JackFunc).funcParams
    }

    fun findParamProps(elem: JackVarReference): List<JackPropertyDefinition> {
        return getFuncParams(elem)?.propertyDefinitionList ?: listOf()
    }
}