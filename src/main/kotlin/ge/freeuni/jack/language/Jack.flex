package ge.freeuni.jack.language.lexer;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.TokenType;import com.intellij.psi.tree.IElementType;
import ge.freeuni.jack.language.JackParserDefinition;import ge.freeuni.jack.language.JackParserDefinition.*;
import ge.freeuni.jack.language.psi.JackTypes;
import groovyjarjarantlr.Token;

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

%state LINE_COMMENT
%state BLOCK_COMMENT


IDENTIFIER=[a-zA-Z_][a-zA-Z_0-9]*
STRING=\"[^\"]*\"
INTEGER=[1-9][0-9]* | 0 

NEW_LINE=(\r|\n|\r\n)+
WHITE_SPACE=(\s|\t)+

LINE_COMMENT= "//" .*
BLOCK_COMMENT = "/*" !([^]* "*/" [^]*) ("*/")?

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
      "=" {return JackTypes.EQ; }
      "do" {return JackTypes.DO;}
      "if" {return JackTypes.IF;}
      "else" {return JackTypes.ELSE;}
      "while" {return JackTypes.WHILE;}
      "return" {return JackTypes.RETURN;}
      "~"  { return JackTypes.NOT;}
      "&"  { return JackTypes.AND;}
      "|"  { return JackTypes.OR;}
      "<"  { return JackTypes.LT;}
      ">"  { return JackTypes.GT;}
      "+"  { return JackTypes.PLUS;}
      "-"  { return JackTypes.MINUS;}
      "*"  { return JackTypes.MUL;}
      "/"  { return JackTypes.DIV;}
      "["  { return JackTypes.LBRACK;}
      "]"  { return JackTypes.RBRACK;}
      "."  { return JackTypes.DOT;}
      "this" {return JackTypes.THIS; }
      "null" { return JackTypes.NULL; }
      "true" { return JackTypes.TRUE; }
      "false" { return JackTypes.FALSE; }
      
      {LINE_COMMENT} { return JackParserDefinition.LINE_COMMENT; }
      {BLOCK_COMMENT} { return JackParserDefinition.BLOCK_COMMENT; }
      
      {STRING} {return JackTypes.STRING; }
      {INTEGER} {return JackTypes.INTEGER; }


  {IDENTIFIER}            { return JackTypes.IDENTIFIER; }
  {WHITE_SPACE} | {NEW_LINE}     { return TokenType.WHITE_SPACE; }
}

//<LINE_COMMENT> {
//	{NEW_LINE} { yypushback(1); yybegin(YYINITIAL); return JackParserDefinition.LINE_COMMENT; }
//    [^{NEW_LINE}] { yypushback(1); }
//}
//
//<BLOCK_COMMENT> {
//	"*/" { yypushback(2); yybegin(YYINITIAL); return JackParserDefinition.BLOCK_COMMENT; }
//    . { yypushback(1); }
//}

[^] { return BAD_CHARACTER; }

