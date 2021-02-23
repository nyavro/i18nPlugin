package com.eny.i18n.plugin.addons.js

import com.eny.i18n.plugin.ide.annotator.JsLanguageFactory
import com.eny.i18n.plugin.ide.completion.CompositeKeyCompletionContributor

/**
 * Completion contributor for JS dialects
 */
class JsCompletionContributor: CompositeKeyCompletionContributor(JsLanguageFactory().callContext())