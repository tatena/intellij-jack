package ge.freeuni.jack.language.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.navigation.ItemPresentation
import com.intellij.usageView.UsageViewUtil
import ge.freeuni.jack.language.psi.JackElement
import javax.swing.Icon

class JackElementImpl(node: ASTNode): ASTWrapperPsiElement(node), JackElement {
    override fun toString(): String {
        return node.elementType.toString()
    }

    override fun getPresentation(): ItemPresentation {
        val text = UsageViewUtil.createNodeText(this)
        return object : ItemPresentation {
            override fun getPresentableText(): String {
                return text
            }

            override fun getLocationString(): String {
                return containingFile.name
            }

            override fun getIcon(b: Boolean): Icon? {
                return null
            }
        }
    }
}