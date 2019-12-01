package com.eny.i18n.plugin.psi;

import com.intellij.psi.tree.IElementType;

public class I18nTokenType extends IElementType {
    public I18nTokenType(String key) {
        super(key, I18nLanguage.INSTANCE);
    }
}
