// This is a generated file. Not intended for manual editing.
package com.eny.i18n.plugin.language.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.eny.i18n.plugin.language.psi.I18nTypes.*;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.eny.i18n.plugin.language.psi.*;

public class I18nCompositeKeyImpl extends ASTWrapperPsiElement implements I18nCompositeKey {

  public I18nCompositeKeyImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull I18nVisitor visitor) {
    visitor.visitCompositeKey(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof I18nVisitor) accept((I18nVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<I18nKeyItem> getKeyItemList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, I18nKeyItem.class);
  }

}
