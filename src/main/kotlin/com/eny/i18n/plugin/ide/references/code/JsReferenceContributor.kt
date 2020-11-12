package com.eny.i18n.plugin.ide.references.code

import com.eny.i18n.plugin.language.js.JsLanguageFactory

/**
 * JS dialect reference contributor
 */
class JsReferenceContributor: ReferenceContributorBase(JsLanguageFactory().referenceAssistant())