package com.eny.i18n.plugin.ide.completion

import com.eny.i18n.plugin.language.vue.VueLanguageFactory

/**
 * Completion contributor for Vue
 */
class VueCompletionContributor: CompositeKeyCompletionContributor(VueLanguageFactory().callContext())