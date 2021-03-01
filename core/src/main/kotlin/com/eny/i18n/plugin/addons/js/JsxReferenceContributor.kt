package com.eny.i18n.plugin.addons.js

import com.eny.i18n.plugin.ide.JsxLanguageFactory
import com.eny.i18n.plugin.ide.references.code.ReferenceContributorBase

/**
 * JSX dialect reference contributor
 */
class JsxReferenceContributor: ReferenceContributorBase(JsxLanguageFactory().referenceAssistant())