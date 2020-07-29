package com.eny.i18n.plugin.ide.completion

import com.eny.i18n.plugin.language.vue.VueCallContext

/**
 * Completion contributor for Vue
 */
class VueCompletionContributor: CompositeKeyCompletionContributor(VueCallContext())