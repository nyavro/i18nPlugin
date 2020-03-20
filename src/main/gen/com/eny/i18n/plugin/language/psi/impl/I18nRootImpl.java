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

public class I18nRootImpl extends ASTWrapperPsiElement implements I18nRoot {

  public I18nRootImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull I18nVisitor visitor) {
    visitor.visitRoot(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof I18nVisitor) accept((I18nVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public I18nFullKey getFullKey() {
    return findChildByClass(I18nFullKey.class);
  }

  @Override
  @Nullable
  public I18nShortKey getShortKey() {
    return findChildByClass(I18nShortKey.class);
  }

}
