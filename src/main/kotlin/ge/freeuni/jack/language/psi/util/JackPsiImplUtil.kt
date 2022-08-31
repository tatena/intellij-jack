package ge.freeuni.jack.language.psi.util

import com.intellij.navigation.ItemPresentation
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.elementType
import com.intellij.util.containers.OrderedSet
import ge.freeuni.jack.language.JackIcons
import ge.freeuni.jack.language.JackUtil
import ge.freeuni.jack.language.psi.*
import ge.freeuni.jack.language.reference.JackReferenceBase
import org.jaxen.expr.VariableReferenceExpr
import javax.swing.Icon

object JackPsiImplUtil {
    @JvmStatic
    fun getClassName(property: JackClassDeclaration): String? {
        val node = property.node.findChildByType(JackTypes.CLASS_NAME_DEFINITION)

        val name = node?.findChildByType(JackTypes.IDENTIFIER)?.text
        return name
    }
    
    @JvmStatic 
    fun getScope(scope: JackPropertyScope): String {
        return when {
            scope.field != null -> "method"
            scope.static != null -> "function"
            else -> throw RuntimeException("invalid scope for given property")
        }
    }
    
    @JvmStatic
    fun getType(prop: JackProperty): String {
        return when {
            prop.primitiveType != null -> prop.primitiveType!!.text
            prop.referenceType != null -> prop.referenceType!!.text
            else -> throw RuntimeException("unresolved type for property")
        }
    }
    @JvmStatic
    fun getType(prop: JackParam): String {
        return when {
            prop.primitiveType != null -> prop.primitiveType!!.text
            prop.referenceType != null -> prop.referenceType!!.text
            else -> throw RuntimeException("unresolved type for property")
        }
    }
    @JvmStatic
    fun getType(prop: JackLocalVars): String {
        return when {
            prop.primitiveType != null -> prop.primitiveType!!.text
            prop.referenceType != null -> prop.referenceType!!.text
            else -> throw RuntimeException("unresolved type for property")
        }
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
        return object : JackReferenceBase(elem, TextRange(0, elem.textLength)) {
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
        return object : JackReferenceBase(elem, TextRange(0, elem.textLength)) {
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
                var fullRef = true
                
                val prevDot = elem.prevSibling
                if (prevDot != null && prevDot.elementType == JackTypes.DOT) {
                    val prevThis = prevDot.prevSibling
                    if (prevThis != null && prevThis.elementType == JackTypes.THIS) {
                        fullRef = false
                    }
                }
                
                if (fullRef) {
                    var isLocal = false
                    var isParam = false
                    val localProps = JackUtil.findLocalProps(elem)
                    for (prop in localProps) {
                        for (def in prop.propertyDefinitionList) {
                            if (def.textMatches(myText)) {
                                myRes.add(def)
                                isLocal = true
                            }
                        }
                    }
                    if (isLocal) return myRes


                    val paramProps = JackUtil.findParamProps(elem)
                    for (def in paramProps) {
                        if (def.textMatches(myText)) {
                            myRes.add(def)
                            isParam = true
                        }
                    }
                    if (isParam) return myRes

                }
                val props = JackUtil.findClassProps(elem)
                for (prop in props) {
                    for (def in prop.propertyDefinitionList) {
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
    
    @JvmStatic
    fun resolve(elem: JackFuncReference): PsiElement? {
        return elem.reference.resolve()
    }

    @JvmStatic
    fun getReference(elem: JackFuncReference): JackReferenceBase {
        val myText = elem.identifier.text
        val myRes = OrderedSet<PsiElement>()
        return object : JackReferenceBase(elem, TextRange(0, elem.textLength)) {
            override fun handleElementRename(newElementName: String): PsiElement? {
                return when (val currentElement = element) {
                    is JackFuncReference -> setName(currentElement, newElementName)
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
                val funcParent = (elem.parent as? JackCallExpr) ?: return listOf()
                if (funcParent.parent is JackAccessExpr) {
                    val access = funcParent.parent as JackAccessExpr
                    val ref = access.dot.prevSibling
                    if (ref.text[0].isUpperCase()) {
                        val jclass = JackUtil.findClass(elem.project, ref.text) ?: return listOf()
                        jclass.staticMethods.forEach { func ->
                            func.funcNameDefinition?.let { name ->
                                if (name.identifier.textMatches(myText)) {
                                    myRes.add(name)
                                }
                            }
                        }
                    } else {
                        if (ref is JackReferenceExpr) {
                            val varRef = ref.varReference
                            val prop = varRef.resolve() ?: return listOf()
                            if (prop is JackPropertyDefinition) {
                                val resolved = prop.parent
                                val classRef: String = when(resolved.elementType) {
                                    JackTypes.PROPERTY -> (resolved as JackProperty).type
                                    JackTypes.PARAM -> (resolved as JackParam).type
                                    JackTypes.LOCAL_VARS -> (resolved as JackLocalVars).type
                                    else -> return listOf()
                                } 
                                val jclass = JackUtil.findClass(elem.project, classRef) ?: return listOf()
                                jclass.memberMethods.forEach { func ->
                                    func.funcNameDefinition?.let { name ->
                                        if (name.identifier.textMatches(myText)) {
                                            myRes.add(name)
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    val methods = JackUtil.getLocalMethods(elem)
                    for (method in methods) {
                        method.funcNameDefinition?.let { name ->
                            if (name.identifier.textMatches(myText)) {
                                myRes.add(name)
                            }
                        }
                    }
                }
                return myRes
            }
        }
    }

    @JvmStatic
    fun setName(elem: JackFuncReference, name: String): PsiElement {
        val e: JackFuncNameDefinition = JackElementFactory.createFunctionDefinition(elem.project, name)
        elem.replace(e)
        return elem
    }
    

    @JvmStatic
    fun isMethod(elem: JackFuncScope): Boolean {
        return elem.method != null
    }
    
    @JvmStatic
    fun isStatic(elem: JackFuncScope): Boolean {
        return elem.function != null
    }
    
    @JvmStatic
    fun getStaticMethods(elem: JackClassDeclaration): List<JackFunc> {
        val body = elem.classBody ?: return listOf()
        return body.funcList.filter { e -> e.funcScope.isStatic }
    }
    @JvmStatic
    fun getMemberMethods(elem: JackClassDeclaration): List<JackFunc> {
        val body = elem.classBody ?: return listOf()
        return body.funcList.filter { e -> e.funcScope.isMethod }
    }
    
    
    
}