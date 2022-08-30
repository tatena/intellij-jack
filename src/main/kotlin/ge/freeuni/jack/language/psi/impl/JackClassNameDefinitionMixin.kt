package ge.freeuni.jack.language.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.navigation.ItemPresentation
import com.intellij.openapi.util.text.StringUtil
import com.intellij.psi.PsiElement
import com.intellij.psi.stubs.IStubElementType
import ge.freeuni.jack.language.JackIcons
import ge.freeuni.jack.language.psi.JackClassNameDefinition
import ge.freeuni.jack.language.psi.util.JackElementFactory
import ge.freeuni.jack.language.stub.JackNamedStubElementBase
import ge.freeuni.jack.language.stub.impl.JackClassNameDefStub
import javax.swing.Icon

abstract class 
JackClassNameDefinitionMixin: JackNamedStubElementBase<JackClassNameDefStub>, JackClassNameDefinition {
    constructor(stub: JackClassNameDefStub, nodeType: IStubElementType<*, *>) : super(stub, nodeType)
    constructor(node: ASTNode) : super(node)

    override fun getName(): String? {
        val stub = stub
        return if (stub != null) StringUtil.notNullize(stub.name) else this.nameIdentifier!!.text
    }

    override fun getNameIdentifier(): PsiElement? {
        return this.node.psi
    }

    override fun setName(name: String): PsiElement {
        val elem = JackElementFactory.createClassNameDef(project, name)
        replace(elem)
        return this
    }

    override fun getPresentation(): ItemPresentation? {
        val outer = this
        return object: ItemPresentation {
            override fun getPresentableText(): String? {
                return outer.identifier.text
            }

            override fun getIcon(unused: Boolean): Icon {
                return JackIcons.FILE
            }

            override fun getLocationString(): String {
                val file = outer.containingFile
                return file.name
            }
        }
    }
}