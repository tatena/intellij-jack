package ge.freeuni.jack.language

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.PsiManager
import com.intellij.psi.search.FileTypeIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.PsiTreeUtil
import ge.freeuni.jack.language.completion.PropertyItem
import ge.freeuni.jack.language.completion.PropertyScope
import ge.freeuni.jack.language.psi.*
import java.util.TreeSet
import java.util.stream.Collectors
import kotlin.streams.toList

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
        val params = getFuncParams(elem)?.paramList ?: return listOf()
        val res = arrayListOf<JackPropertyDefinition>()
        for (param in params) {
            param.propertyDefinition?.let { res.add(it) }
        }
        return res
    }

    fun getLocalMethods(elem: JackFuncReference): List<JackFunc> {
        val file = elem.containingFile
        val jclass = PsiTreeUtil.findChildOfType(file, JackClassDeclaration::class.java) ?: return listOf()
        
        val funcs = jclass.classBody?.funcList ?: return listOf()
        
        return funcs.filter { e -> e.funcScope.isMethod }
    }

    fun getAllVariables(elem: JackVarReference): Set<PropertyItem> {
        val res = mutableSetOf<PropertyItem>()
        
        val locals = findLocalProps(elem)
        locals.forEach { localDef ->
            val type = localDef.type
            localDef.propertyDefinitionList.forEach { def ->
                res.add(PropertyItem(
                    def.identifier.text,
                    type,
                    PropertyScope.LOCAL
                ))
            }
        }
        
        val params = getFuncParams(elem)?.paramList
        params?.forEach { param ->
            param.propertyDefinition?.let { def ->
                res.add(PropertyItem(
                    def.text,
                    param.type,
                    PropertyScope.PARAM
                ))
            }
        }
        
        val props = findClassProps(elem)
        props.forEach { defs ->
            val type = defs.type
            val scope = defs.propertyScope.scope
            defs.propertyDefinitionList.forEach { def ->
                res.add(PropertyItem(
                    def.identifier.text,
                    type,
                    PropertyScope.resolveScope(scope)
                ))
            }
        }
        
        return res
    }
}