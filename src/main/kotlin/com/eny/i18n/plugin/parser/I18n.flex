package com.eny.i18n.plugin.parser;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;

import static com.intellij.psi.TokenType.BAD_CHARACTER;
import static com.intellij.psi.TokenType.WHITE_SPACE;
import static com.eny.i18n.plugin.psi.I18nTypes.*;

%%

%{
  public I18nLexer() {
    this((java.io.Reader)null);
  }
%}

%public
%class I18nLexer
%implements FlexLexer
%function advance
%type IElementType
%unicode

EOL=\R
WHITE_SPACE=\s+


%%
<YYINITIAL> {
  {WHITE_SPACE}      { return WHITE_SPACE; }

  "key"              { return KEY; }
  "separator"        { return SEPARATOR; }


}

[^] { return BAD_CHARACTER; }
