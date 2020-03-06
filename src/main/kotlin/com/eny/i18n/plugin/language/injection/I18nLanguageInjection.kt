package com.eny.i18n.plugin.language.injection

import com.eny.i18n.plugin.language.psi.I18nFullKey
import com.intellij.lang.Language
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiLanguageInjectionHost


class I18nLanguageInjection {

    fun addInjectionInPlace(language: Language?, psiElement: PsiLanguageInjectionHost?): Boolean {
        return false
    }

    fun removeInjectionInPlace(psiElement: PsiLanguageInjectionHost?): Boolean {
        return false
    }

    fun editInjectionInPlace(psiElement: PsiLanguageInjectionHost?): Boolean {
        return false
    }

//    fun createInjection(element: Element?): BaseInjection? {
//        return BaseInjection(getId())
//    }

//    fun setupPresentation(injection: BaseInjection, presentation: SimpleColoredText, isSelected: Boolean) {
//        presentation.append(injection.getDisplayName(), SimpleTextAttributes.REGULAR_ATTRIBUTES)
//    }

//    fun createSettings(project: Project?, configuration: Configuration?): Array<Configurable?>? {
//        return arrayOfNulls<Configurable>(0)
//    }

//    fun createAddActions(project: Project?, consumer: Consumer<BaseInjection?>?): Array<AnAction?>? {
//        return AnAction.EMPTY_ARRAY
//    }

//    fun createEditAction(project: Project?, producer: Factory<BaseInjection?>?): AnAction? {
//        return null
//    }

    fun getId(): String? {
        return "I18n"
    }

//    fun getPatternClasses(): Array<Class<*>?>? {
//        return arrayOf(GroovyPatterns::class.java)
//    }

    fun useDefaultInjector(host: PsiElement?): Boolean {
        return host is I18nFullKey
    }
}