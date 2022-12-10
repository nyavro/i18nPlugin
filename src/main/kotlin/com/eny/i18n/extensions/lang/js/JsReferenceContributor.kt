package com.eny.i18n.extensions.lang.js

import com.eny.i18n.plugin.ide.references.code.ReferenceContributorBase
import com.eny.i18n.plugin.language.js.JsLanguageFactory

/**
 * JS dialect reference contributor
 */
class JsReferenceContributor: ReferenceContributorBase(JsReferenceAssistant())