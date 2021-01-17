package com.eny.i18n.plugin.addons.technology.vue

import com.eny.i18n.plugin.ide.references.code.ReferenceContributorBase

/**
 * Vue dialect reference contributor
 */
class VueReferenceContributor: ReferenceContributorBase(VueLanguageFactory().referenceAssistant())