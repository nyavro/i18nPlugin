package com.eny.i18n.plugin.ide.refactoring

import com.eny.i18n.plugin.parser.KeyExtractorImpl
import com.eny.i18n.plugin.tree.CompositeKeyResolver
import com.eny.i18n.plugin.utils.unQuote
import com.intellij.json.psi.JsonProperty
import com.intellij.lang.Language
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.impl.FakePsiElement
import com.intellij.psi.search.SearchScope
import com.intellij.psi.tree.IElementType
import com.intellij.refactoring.rename.RenamePsiElementProcessor

class PsiRef(private val element: CurrentPsi): PsiReferenceBase<PsiElement>(element) {
    override fun resolve(): PsiElement? {
        return element.element
    }
}

class CurrentPsi(val element: PsiElement): FakePsiElement() {

    val nameElement = (element.parent as? JsonProperty)?.nameElement

    override fun getParent(): PsiElement {
        return element.parent
    }

    override fun getName(): String? {
        return nameElement?.text?.unQuote()
    }

    override fun getTextRange(): TextRange? {
        return nameElement?.textRange
    }
}

class RenameContributor: RenamePsiElementProcessor(), CompositeKeyResolver<PsiElement> {

    val extractor = KeyExtractorImpl()

    override fun canProcessElement(element: PsiElement): Boolean {
        return true;
    }

    override fun prepareRenaming(element: PsiElement, newName: String, allRenames: MutableMap<PsiElement, String>) {
        super.prepareRenaming(element, newName, allRenames)
    }

    override fun findReferences(element: PsiElement, searchScope: SearchScope, searchInCommentsAndStrings: Boolean): MutableCollection<PsiReference> {
//        val i18nKey = extractor.extractI18nKeyLiteral(element)
//        if (i18nKey != null) {
//            val settings = Settings.getInstance(element.project)
//            val files = LocalizationFileSearch(element.project).findFilesByName(i18nKey.ns?.text)
//            files
//                .flatMap { jsonFile -> resolve(i18nKey.compositeKey, PsiElementTree.create(jsonFile), settings.pluralSeparator) }
//            val res = resolve(i18nKey.compositeKey, PsiElementTree.create())
//        }
//        return super.findReferences(element, searchScope, searchInCommentsAndStrings)
        return if (element is CurrentPsi) mutableListOf(PsiRef(element)) else mutableListOf()
    }

    override fun substituteElementToRename(element: PsiElement, editor: Editor?): PsiElement? {
        return CurrentPsi(element)
    }
}