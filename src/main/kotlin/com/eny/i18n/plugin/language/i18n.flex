package com.eny.i18n.plugin.language;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import com.eny.i18n.plugin.language.psi.I18nTypes;
import com.intellij.psi.TokenType;

%%

%class I18nLexer
%implements FlexLexer
%unicode
%function advance
%type IElementType
%eof{  return;
%eof}
KEY=[^:\.\s]
NS_SEPARATOR=[:]
KEY_SEPARATOR=[\.]

%state WAITING_KEY
%state WAITING_SEPARATOR

%%

<YYINITIAL> {KEY}+                                         { yybegin(WAITING_SEPARATOR);   return I18nTypes.KEY; }
<WAITING_SEPARATOR> {NS_SEPARATOR}                         { yybegin(WAITING_KEY); return I18nTypes.NS_SEPARATOR; }
<WAITING_SEPARATOR> {KEY_SEPARATOR}                        { yybegin(WAITING_KEY); return I18nTypes.KEY_SEPARATOR; }
<WAITING_KEY> {KEY}+                                       { yybegin(WAITING_KEY); return I18nTypes.KEY; }
<WAITING_KEY> {KEY_SEPARATOR}                              { yybegin(WAITING_KEY); return I18nTypes.KEY_SEPARATOR; }

[^]                                                        { return TokenType.BAD_CHARACTER; }
