package ge.freeuni.jack.language.psi.impl

import com.intellij.dupLocator.treeView.NodeMatcher
import com.intellij.psi.PsiElement
import ge.freeuni.jack.language.psi.JackClassDeclaration
import ge.freeuni.jack.language.psi.JackClassNameDefinition
import ge.freeuni.jack.language.psi.JackElementFactory
import ge.freeuni.jack.language.psi.JackTypes

object JackPsiImplUtil {
    @JvmStatic
    fun getClassName(property: JackClassDeclaration): String? {
        val node = property.node.findChildByType(JackTypes.CLASS_NAME_DEFINITION)

        val name = node?.findChildByType(JackTypes.IDENTIFIER)?.text
        return name
    }
    
    fun getClassName(property: JackClassNameDefinition): String? {
        return property.identifier.text
    }
    
    fun setName(element: JackClassNameDefinition, name: String): PsiElement {
        val optNode = element.node.findChildByType(JackTypes.IDENTIFIER)
        optNode?.let { node ->
            val def = JackElementFactory.createClassNameDef(element.project, name)
            val newIdent = def.identifier.node
            element.node.replaceChild(node, newIdent)
        }
        return element
    }

    fun getNameIdentifier(element: JackClassNameDefinition): PsiElement? {
        return element.node.findChildByType(JackTypes.IDENTIFIER)?.psi
    }
}