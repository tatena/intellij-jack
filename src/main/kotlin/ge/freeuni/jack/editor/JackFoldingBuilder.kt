package ge.freeuni.jack.editor

import com.intellij.lang.ASTNode
import com.intellij.lang.folding.FoldingBuilderEx
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.editor.Document
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.elementType
import ge.freeuni.jack.language.JackUtil
import ge.freeuni.jack.language.psi.JackClassDeclaration

class JackFoldingBuilder : FoldingBuilderEx(), DumbAware {
    override fun buildFoldRegions(root: PsiElement, document: Document, quick: Boolean): Array<FoldingDescriptor> {
        val descriptors = arrayListOf<FoldingDescriptor>()
//        val classes = JackUtil.findClasses(root.project)
//        
//        for (jclass in classes) {
//            
//            val body = (jclass.parent as JackClassDeclaration).classBody
//            body?.let { 
//                descriptors.add(
//                    FoldingDescriptor(it.node, TextRange(it.textRange.startOffset + 1, it.textRange.endOffset - 1))
//                )
//            }
//        }
        val jclass = PsiTreeUtil.findChildOfType(root, JackClassDeclaration::class.java)
        if (jclass != null) {
            val optBody = jclass.classBody
            optBody?.let { body ->
                descriptors.add(FoldingDescriptor(body.node, body.textRange))
            }
        }

        return descriptors.toTypedArray()
    }

    override fun getPlaceholderText(node: ASTNode): String? {
        return " { ... } "
    }

    override fun isCollapsedByDefault(node: ASTNode): Boolean = false
}