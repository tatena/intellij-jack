package ge.freeuni.jack.editor

import com.intellij.formatting.*
import com.intellij.lang.ASTNode
import com.intellij.psi.TokenType
import com.intellij.psi.formatter.common.AbstractBlock
import com.intellij.psi.tree.IElementType
import com.intellij.psi.util.elementType
import ge.freeuni.jack.language.psi.JackTypes

class JackFormattingBlock(
    node: ASTNode,
    wrap: Wrap? = null,
    alignment: Alignment? = null,
    private val mySpacingBuilder: SpacingBuilder,
    private val myIndent: Indent = Indent.getNoneIndent()
) : AbstractBlock(node, wrap, alignment) {

    override fun getIndent(): Indent {
        return myIndent
    }

    override fun getSpacing(child1: Block?, child2: Block): Spacing? = mySpacingBuilder.getSpacing(this, child1, child2)

    override fun isLeaf(): Boolean = myNode.firstChildNode == null


    override fun getChildIndent(): Indent? {
        return when(node.elementType) {
            JackTypes.FUNC_BODY, 
            JackTypes.CLASS_BODY -> Indent.getNormalIndent()
            else -> Indent.getNoneIndent()
        }
    }
    
    override fun buildChildren(): MutableList<Block> {
        val blocks = arrayListOf<Block>()
        var child = myNode.firstChildNode
        while (child != null) {
            val type = child.elementType

            if (type != TokenType.WHITE_SPACE) {
//                println("${node.psi.containingFile.name}: $iter - ${child.elementType}")
                val ident = calcIndent(type)
//                when (type) {
//                    is JackClassDeclaration, is JackProperty, is JackFunc, is JackLocalVars, is JackStmt -> {
                val block = JackFormattingBlock(
                    child,
                    mySpacingBuilder = mySpacingBuilder,
                    myIndent = ident
                )
                blocks.add(block)
//                    }
//                }
            }
            child = child.treeNext
        }
        return blocks
    }

    private fun calcIndent(type: IElementType): Indent {
        return when (type) {
            JackTypes.LOCAL_VARS, JackTypes.STMT,
            JackTypes.PROPERTY, JackTypes.FUNC -> Indent.getNormalIndent()
            else -> Indent.getNoneIndent()
        }
    }
}