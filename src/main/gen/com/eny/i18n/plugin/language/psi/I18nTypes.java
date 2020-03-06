// This is a generated file. Not intended for manual editing.
package com.eny.i18n.plugin.language.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import com.eny.i18n.plugin.language.psi.impl.*;

public interface I18nTypes {

  IElementType COMPOSITE_KEY = new I18nElementType("COMPOSITE_KEY");
  IElementType FULL_KEY = new I18nElementType("FULL_KEY");
  IElementType NAMESPACE = new I18nElementType("NAMESPACE");
  IElementType SHORT_KEY = new I18nElementType("SHORT_KEY");

  IElementType KEY = new I18nTokenType("KEY");
  IElementType KEY_SEPARATOR = new I18nTokenType("KEY_SEPARATOR");
  IElementType NS_SEPARATOR = new I18nTokenType("NS_SEPARATOR");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
      if (type == COMPOSITE_KEY) {
        return new I18nCompositeKeyImpl(node);
      }
      else if (type == FULL_KEY) {
        return new I18nFullKeyImpl(node);
      }
      else if (type == NAMESPACE) {
        return new I18nNamespaceImpl(node);
      }
      else if (type == SHORT_KEY) {
        return new I18nShortKeyImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
