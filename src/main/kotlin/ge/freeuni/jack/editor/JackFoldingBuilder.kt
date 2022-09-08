package ge.freeuni.jack.editor

import com.intellij.lang.ASTNode
import com.intellij.lang.folding.FoldingBuilderEx
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.editor.Document
import com.intellij.openapi.project.DumbAware
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import ge.freeuni.jack.language.psi.JackClassDeclaration
import ge.freeuni.jack.language.psi.JackIfStatement
import ge.freeuni.jack.language.psi.JackStatement
import ge.freeuni.jack.language.psi.JackWhileStatement

class JackFoldingBuilder : FoldingBuilderEx(), DumbAware {
    
    private fun handleStatements(stmts: List<JackStatement>, descriptors: ArrayList<FoldingDescriptor>) {
        stmts.forEach { outerStmt ->
            val stmt = outerStmt.firstChild
            when (stmt) {
                is JackWhileStatement -> {
                    stmt.whileBody?.let { whileBody ->
                        descriptors.add(FoldingDescriptor(whileBody.node, whileBody.textRange))
                        handleStatements(whileBody.statementList, descriptors)
                    }
                }
                is JackIfStatement -> {
                    stmt.ifBody?.let { ifBody ->
                        descriptors.add(FoldingDescriptor(ifBody.node, ifBody.textRange))
                        handleStatements(ifBody.statementList, descriptors)
                    }
                    stmt.elseStatement?.elseBody?.let { elseBody ->
                        descriptors.add(FoldingDescriptor(elseBody.node, elseBody.textRange))
                        handleStatements(elseBody.statementList, descriptors)
                    }
                }
            }
        }
    }
    
    override fun buildFoldRegions(root: PsiElement, document: Document, quick: Boolean): Array<FoldingDescriptor> {
        val descriptors = arrayListOf<FoldingDescriptor>()

        val jclass = PsiTreeUtil.findChildOfType(root, JackClassDeclaration::class.java)
        if (jclass != null) {
            val optBody = jclass.classBody
            optBody?.let { body ->
                descriptors.add(FoldingDescriptor(body.node, body.textRange))
                
                body.funcList.forEach { func ->
                    func.funcBody?.let { funcBody ->
                        descriptors.add(FoldingDescriptor(funcBody.node, funcBody.textRange))
                        handleStatements(funcBody.statementList, descriptors)
                    }
                }
            }
        }

        return descriptors.toTypedArray()
    }

    override fun getPlaceholderText(node: ASTNode): String? {
        return " { ... } "
    }

    override fun isCollapsedByDefault(node: ASTNode): Boolean = false
}