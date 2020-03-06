// This is a generated file. Not intended for manual editing.
package com.eny.i18n.plugin.language.parser;

import com.intellij.lang.ASTNode;
import com.intellij.lang.LightPsiParser;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import com.intellij.lang.PsiParser;
import com.intellij.psi.tree.IElementType;

import static com.eny.i18n.plugin.language.psi.I18nTypes.*;
import static com.intellij.lang.parser.GeneratedParserUtilBase.*;

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
    return i18n(b, l + 1);
  }

  /* ********************************************************** */
  // KEY (KEY_SEPARATOR KEY)*
  public static boolean compositeKey(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "compositeKey")) return false;
    if (!nextTokenIs(b, KEY)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, KEY);
    r = r && compositeKey_1(b, l + 1);
    exit_section_(b, m, COMPOSITE_KEY, r);
    return r;
  }

  // (KEY_SEPARATOR KEY)*
  private static boolean compositeKey_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "compositeKey_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!compositeKey_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "compositeKey_1", c)) break;
    }
    return true;
  }

  // KEY_SEPARATOR KEY
  private static boolean compositeKey_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "compositeKey_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, KEY_SEPARATOR, KEY);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // namespace NS_SEPARATOR compositeKey
  public static boolean fullKey(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "fullKey")) return false;
    if (!nextTokenIs(b, KEY)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = namespace(b, l + 1);
    r = r && consumeToken(b, NS_SEPARATOR);
    r = r && compositeKey(b, l + 1);
    exit_section_(b, m, FULL_KEY, r);
    return r;
  }

  /* ********************************************************** */
  // fullKey | shortKey
  static boolean i18n(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "i18n")) return false;
    if (!nextTokenIs(b, KEY)) return false;
    boolean r;
    r = fullKey(b, l + 1);
    if (!r) r = shortKey(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // KEY
  public static boolean namespace(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "namespace")) return false;
    if (!nextTokenIs(b, KEY)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, KEY);
    exit_section_(b, m, NAMESPACE, r);
    return r;
  }

  /* ********************************************************** */
  // compositeKey
  public static boolean shortKey(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "shortKey")) return false;
    if (!nextTokenIs(b, KEY)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = compositeKey(b, l + 1);
    exit_section_(b, m, SHORT_KEY, r);
    return r;
  }

}
