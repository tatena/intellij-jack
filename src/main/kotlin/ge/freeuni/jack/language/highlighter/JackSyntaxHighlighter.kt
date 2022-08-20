package ge.freeuni.jack.language.highlighter

import com.fasterxml.jackson.databind.JavaType
import com.intellij.lang.ParserDefinition
import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.tree.IElementType
import ge.freeuni.jack.language.JackLexerAdapter
import ge.freeuni.jack.language.JackParserDefinition
import ge.freeuni.jack.language.psi.JackTypes
import org.jetbrains.annotations.NotNull

class JackSyntaxHighlighter : SyntaxHighlighterBase() {
    private val attributes: Map<IElementType, TextAttributesKey> = HashMap()

    companion object {
        val KEYWORD: TextAttributesKey =
            createTextAttributesKey("KEYWORD", DefaultLanguageHighlighterColors.KEYWORD)
        val IDENTIFIER: TextAttributesKey =
            createTextAttributesKey("IDENTIFIER", DefaultLanguageHighlighterColors.IDENTIFIER)

        val ANNOTATION: TextAttributesKey =
            createTextAttributesKey("CLASS_NAME", DefaultLanguageHighlighterColors.METADATA)
        val INTEGER: TextAttributesKey = 
            createTextAttributesKey("INTEGER", DefaultLanguageHighlighterColors.NUMBER)
        val STRING: TextAttributesKey = 
            createTextAttributesKey("STRING", DefaultLanguageHighlighterColors.STRING)
        val OP: TextAttributesKey = 
            createTextAttributesKey("OPERATOR", DefaultLanguageHighlighterColors.DOT)
        val COMMENT: TextAttributesKey = 
            createTextAttributesKey("COMMENT", DefaultLanguageHighlighterColors.BLOCK_COMMENT)

        private val KEYWORDS = arrayOf(KEYWORD)

        private val IDENTIFIERS = arrayOf(IDENTIFIER, ANNOTATION, IDENTIFIER)
        
        private val INTEGERS = arrayOf(INTEGER)
        private val STRINGS = arrayOf(STRING)
        private val OPS = arrayOf(OP)
        private val COMMENTS = arrayOf(COMMENT)
    }


    override fun getHighlightingLexer(): Lexer {
        return JackLexerAdapter()
    }

    override fun getTokenHighlights(tokenType: IElementType): @NotNull Array<TextAttributesKey> {
        return when (tokenType) {
            JackTypes.INT, JackTypes.BOOLEAN, JackTypes.CHAR,
            JackTypes.STATIC, JackTypes.FIELD, JackTypes.JCLASS,
            JackTypes.METHOD, JackTypes.FUNCTION, JackTypes.VOID,
            JackTypes.CONSTRUCTOR, JackTypes.LET, JackTypes.VAR,
            JackTypes.DO, JackTypes.IF, JackTypes.ELSE, JackTypes.WHILE,
            JackTypes.RETURN, 
            -> KEYWORDS
            JackTypes.IDENTIFIER,
            JackTypes.SEMICOLON,
            JackTypes.EQ 
            -> IDENTIFIERS
            JackTypes.GT, JackTypes.LT, JackTypes.MINUS, JackTypes.DOT, JackTypes.PLUS,
            JackTypes.MUL, JackTypes.DIV, JackTypes.NOT, JackTypes.EQ 
            -> OPS
            JackTypes.INTEGER -> INTEGERS
            JackTypes.STRING -> STRINGS
            JackParserDefinition.BLOCK_COMMENT, JackParserDefinition.LINE_COMMENT
            -> COMMENTS
            else -> IDENTIFIERS
        }
    }


}