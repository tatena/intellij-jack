package ge.freeuni.jack.language.highlighter

import com.fasterxml.jackson.databind.JavaType
import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.tree.IElementType
import ge.freeuni.jack.language.JackLexerAdapter
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
        
        private val KEYWORDS = arrayOf(KEYWORD)
        
        private val IDENTIFIERS = arrayOf(IDENTIFIER)
    }



    override fun getHighlightingLexer(): Lexer {
        return JackLexerAdapter()
    }

    override fun getTokenHighlights(tokenType: IElementType): @NotNull Array<TextAttributesKey> {
        return when (tokenType) {
            JackTypes.INT,
            JackTypes.BOOLEAN,
            JackTypes.CHAR,
            JackTypes.STATIC,
            JackTypes.FIELD ->
                KEYWORDS
            JackTypes.IDENTIFIER,
            JackTypes.SEMICOLON ->
                IDENTIFIERS
            else -> IDENTIFIERS
        }
    }


}