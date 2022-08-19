package ge.freeuni.jack.language.lexer;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.TokenType;import com.intellij.psi.tree.IElementType;
import ge.freeuni.jack.language.psi.JackTypes;

import static com.intellij.psi.TokenType.BAD_CHARACTER;
import static com.intellij.psi.TokenType.WHITE_SPACE;

%%

%{
  public JackLexer() {
    this((java.io.Reader)null);
  }
%}

%public
%class JackLexer
%implements FlexLexer
%function advance
%type IElementType
%unicode

EOL=\R

IDENTIFIER=[a-zA-Z_]+

NEW_LINE=(\r|\n|\r\n)+
WHITE_SPACE=(\s|\t)+

%%
<YYINITIAL> {

"class" 	{return JackTypes.JCLASS; }
  "field"                { return JackTypes.FIELD; }
  "static"                { return JackTypes.STATIC; }
  "boolean"                { return JackTypes.BOOLEAN; }
  "char"                { return JackTypes.CHAR; }
  "int"                { return JackTypes.INT; }
      "void" 	{ return JackTypes.VOID; }
  ";"                { return JackTypes.SEMICOLON; }
      "{" {return JackTypes.LBRACE; }
      "}" {return JackTypes.RBRACE; }
      "(" {return JackTypes.LPAREN; }
      ")" {return JackTypes.RPAREN; }
      "function" {return JackTypes.FUNCTION; }
      "method" {return JackTypes.METHOD; }
      "constructor" {return JackTypes.CONSTRUCTOR; }
      "," {return JackTypes.COMMA; }
      "let" {return JackTypes.LET; }
      "var" {return JackTypes.VAR; }


  {IDENTIFIER}            { return JackTypes.IDENTIFIER; }
  {WHITE_SPACE} | {NEW_LINE}     { return TokenType.WHITE_SPACE; }
}

[^] { return BAD_CHARACTER; }

