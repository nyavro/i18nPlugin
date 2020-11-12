package com.eny.i18n.plugin.ide.references.code

import com.eny.i18n.plugin.language.jsx.JsxLanguageFactory

/**
 * JSX dialect reference contributor
 */
class JsxReferenceContributor: ReferenceContributorBase(JsxLanguageFactory().referenceAssistant())