// This is a generated file. Not intended for manual editing.
package com.eny.i18n.plugin.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import static com.eny.i18n.plugin.psi.I18nTypes.*;
import static com.eny.i18n.plugin.parser.I18nParserUtil.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import com.intellij.lang.PsiParser;
import com.intellij.lang.LightPsiParser;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class I18nParser implements PsiParser, LightPsiParser {

  public ASTNode parse(IElementType t, PsiBuilder b) {
    parseLight(t, b);
    return b.getTreeBuilt();
  }

  public void parseLight(IElementType t, PsiBuilder b) {
    boolean r;
    b = adapt_builder_(t, b, this, null);
    Marker m = enter_section_(b, 0, _COLLAPSE_, null);
    r = parse_root_(t, b);
    exit_section_(b, 0, m, t, r, true, TRUE_CONDITION);
  }

  protected boolean parse_root_(IElementType t, PsiBuilder b) {
    return parse_root_(t, b, 0);
  }

  static boolean parse_root_(IElementType t, PsiBuilder b, int l) {
    return i18nFile(b, l + 1);
  }

  /* ********************************************************** */
  // item
  static boolean i18nFile(PsiBuilder b, int l) {
    return item(b, l + 1);
  }

  /* ********************************************************** */
  // ns? key (separator key)*
  static boolean item(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "item")) return false;
    if (!nextTokenIs(b, KEY)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = item_0(b, l + 1);
    r = r && consumeToken(b, KEY);
    r = r && item_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ns?
  private static boolean item_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "item_0")) return false;
    ns(b, l + 1);
    return true;
  }

  // (separator key)*
  private static boolean item_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "item_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!item_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "item_2", c)) break;
    }
    return true;
  }

  // separator key
  private static boolean item_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "item_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, SEPARATOR, KEY);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // key separator
  static boolean ns(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ns")) return false;
    if (!nextTokenIs(b, KEY)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, KEY, SEPARATOR);
    exit_section_(b, m, null, r);
    return r;
  }

}
