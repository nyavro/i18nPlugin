package com.eny.i18n.plugin.ide.completion

import com.eny.i18n.plugin.language.js.JsLanguageFactory

/**
 * Completion contributor for JS dialects
 */
class JsCompletionContributor: CompositeKeyCompletionContributor(JsLanguageFactory().callContext())