package ge.freeuni.jack.language

import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.lang.PsiParser
import com.intellij.lexer.Lexer
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.TokenSet
import ge.freeuni.jack.language.parser.JackParser
import ge.freeuni.jack.language.psi.JackFile
import ge.freeuni.jack.language.psi.JackTypes
import ge.freeuni.jack.language.psi.type.JackTokenType

class JackParserDefinition : ParserDefinition {
    companion object {
        
        @JvmField val FILE = IFileElementType(JackLanguage.INSTANCE)
        @JvmField val WHITE_SPACES = TokenSet.create(TokenType.WHITE_SPACE)
        @JvmField val LINE_COMMENT = JackTokenType("<LINE_COMMENT>")
        @JvmField val BLOCK_COMMENT = JackTokenType("<BLOCK_COMMENT>")
    }

    override fun createLexer(project: Project?): Lexer {
        return JackLexerAdapter()
    }

    override fun createParser(project: Project?): PsiParser {
        return JackParser()
    }

    override fun getFileNodeType(): IFileElementType {
        return FILE
    }

    override fun getCommentTokens(): TokenSet {
        return TokenSet.create(LINE_COMMENT, BLOCK_COMMENT)
    }

    override fun getStringLiteralElements(): TokenSet {
        return TokenSet.create(JackTypes.STRING)
    }

    override fun createElement(node: ASTNode?): PsiElement {
        return JackTypes.Factory.createElement(node)
    }

    override fun createFile(viewProvider: FileViewProvider): PsiFile {
        return JackFile(viewProvider)
    }

    override fun spaceExistenceTypeBetweenTokens(left: ASTNode?, right: ASTNode?): ParserDefinition.SpaceRequirements {
        return ParserDefinition.SpaceRequirements.MAY
    }

    override fun getWhitespaceTokens(): TokenSet {
        return WHITE_SPACES
    }
}