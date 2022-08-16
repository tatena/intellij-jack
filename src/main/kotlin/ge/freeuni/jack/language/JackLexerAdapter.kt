package ge.freeuni.jack.language

import com.intellij.lexer.FlexAdapter
import ge.freeuni.jack.language.lexer.JackLexer

class JackLexerAdapter : FlexAdapter(JackLexer(null))