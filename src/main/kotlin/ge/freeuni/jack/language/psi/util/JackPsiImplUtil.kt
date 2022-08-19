package ge.freeuni.jack.language.psi.util

import com.intellij.navigation.ItemPresentation
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.containers.OrderedSet
import ge.freeuni.jack.language.JackIcons
import ge.freeuni.jack.language.JackUtil
import ge.freeuni.jack.language.psi.JackClassDeclaration
import ge.freeuni.jack.language.psi.JackClassNameDefinition
import ge.freeuni.jack.language.psi.JackProperty
import ge.freeuni.jack.language.psi.JackPropertyDefinition
import ge.freeuni.jack.language.psi.JackReferenceType
import ge.freeuni.jack.language.psi.JackTypes
import ge.freeuni.jack.language.psi.JackVarReference
import ge.freeuni.jack.language.reference.JackReferenceBase
import javax.swing.Icon

object JackPsiImplUtil {
    @JvmStatic
    fun getClassName(property: JackClassDeclaration): String? {
        val node = property.node.findChildByType(JackTypes.CLASS_NAME_DEFINITION)

        val name = node?.findChildByType(JackTypes.IDENTIFIER)?.text
        return name
    }
//    
//    fun getClassName(property: JackClassNameDefinition): String? {
//        return property.identifier.text
//    }

    @JvmStatic
    fun setName(element: JackClassNameDefinition, name: String): PsiElement {
        val optNode = element.node.findChildByType(JackTypes.IDENTIFIER)
        optNode?.let { node ->
            val def = JackElementFactory.createClassNameDef(element.project, name)
            val newIdent = def.identifier.node
            element.node.replaceChild(node, newIdent)
        }
        return element
    }
    @JvmStatic
    fun getNameIdentifier(element: JackClassNameDefinition): PsiElement? {
        return element.node.findChildByType(JackTypes.IDENTIFIER)?.psi
    }
    @JvmStatic
    fun setName(elem: JackReferenceType, name: String): PsiElement {
        val e: JackReferenceType = JackElementFactory.createPropertyFromText(elem.project, name)
        elem.replace(e)
        return elem
    } 
    
    @JvmStatic
    fun getReference(elem: JackReferenceType): JackReferenceBase {
        val myText = elem.identifier.text
        val myRes = OrderedSet<PsiElement>()
        return object: JackReferenceBase(elem, TextRange(0, elem.textLength)) {
            override fun handleElementRename(newElementName: String): PsiElement? {
                return when (val currentElement = element) {
                    is JackReferenceType -> setName(currentElement, newElementName)
                    else -> return null
                }
            }

            override fun isReferenceTo(element: PsiElement): Boolean {
                val resolved = resolve()
                val manager = getElement().manager
                return manager.areElementsEquivalent(resolved, element)
                        || manager.areElementsEquivalent(resolved?.parent, element)
            }

            override fun resolveInner(incompleteCode: Boolean): List<PsiElement> {
                val classes = JackUtil.findClasses(elem.project)
                for (jclass in classes) {
                    if (jclass.identifier.textMatches(myText)) {
                        myRes.add(jclass)
                    }
                }
                return myRes
            }
        }
    }
    
    @JvmStatic
    fun setName(elem: JackVarReference, name: String): PsiElement {
        val e: JackPropertyDefinition = JackElementFactory.createPropertyDefinition(elem.project, name)
        elem.replace(e)
        return elem
    }

    @JvmStatic
    fun getReference(elem: JackVarReference): JackReferenceBase {
        val myText = elem.identifier.text
        val myRes = OrderedSet<PsiElement>()
        return object: JackReferenceBase(elem, TextRange(0, elem.textLength)) {
            override fun handleElementRename(newElementName: String): PsiElement? {
                return when (val currentElement = element) {
                    is JackVarReference -> setName(currentElement, newElementName)
                    else -> return null
                }
            }

            override fun isReferenceTo(element: PsiElement): Boolean {
                val resolved = resolve()
                val manager = getElement().manager
                return manager.areElementsEquivalent(resolved, element)
                        || manager.areElementsEquivalent(resolved?.parent, element)
            }

            override fun resolveInner(incompleteCode: Boolean): List<PsiElement> {
                var isLocal = false
                val localProps = JackUtil.findLocalProps(elem)
                for (prop in localProps) {
                    prop.propertyDefinition?.let { def ->
                        if (def.textMatches(myText)) {
                            myRes.add(def)
                            isLocal = true
                        }
                    }
                }
                if (isLocal) {
                    return myRes
                }
                
                val props = JackUtil.findClassProps(elem)
                for (prop in props) {
                    prop.propertyDefinition?.let { def ->
                        if (def.textMatches(myText)) {
                            myRes.add(def)
                        }
                    }
                }
                return myRes
            }
        }
    }
    
    @JvmStatic
    fun resolve(elem: JackReferenceType): PsiElement? {
        return elem.reference.resolve()
    }
    
    

    @JvmStatic
    fun resolve(elem: JackVarReference): PsiElement? {
        return elem.reference.resolve()
    }
}