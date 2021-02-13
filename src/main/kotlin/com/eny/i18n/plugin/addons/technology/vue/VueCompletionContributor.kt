package com.eny.i18n.plugin.addons.technology.vue

import com.eny.i18n.plugin.ide.completion.CompositeKeyCompletionContributor

/**
 * Completion contributor for Vue
 */
class VueCompletionContributor: CompositeKeyCompletionContributor(VueLanguageFactory().callContext())