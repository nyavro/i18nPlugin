package com.eny.i18n.plugin.ide.completion

import com.eny.i18n.plugin.language.js.JsCallContext

/**
 * Completion contributor for JS dialects
 */
class JsCompletionContributor: CompositeKeyCompletionContributor(JsCallContext())