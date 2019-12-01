package com.eny.i18n.plugin.psi;

import com.intellij.lang.Language;
import org.jetbrains.annotations.NotNull;

public class I18nLanguage extends Language {
    public static final I18nLanguage INSTANCE = new I18nLanguage();
    protected I18nLanguage() {
        super("I18n");
    }
}
