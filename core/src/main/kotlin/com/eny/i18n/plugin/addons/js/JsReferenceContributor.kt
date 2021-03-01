package com.eny.i18n.plugin.addons.js

import com.eny.i18n.plugin.ide.JsLanguageFactory
import com.eny.i18n.plugin.ide.references.code.ReferenceContributorBase

/**
 * JS dialect reference contributor
 */
class JsReferenceContributor: ReferenceContributorBase(JsLanguageFactory().referenceAssistant())