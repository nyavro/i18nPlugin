package com.eny.i18n.plugin.ide.annotator

import com.eny.i18n.plugin.language.vue.VueCallContext
import com.eny.i18n.plugin.key.FullKeyExtractor
import com.eny.i18n.plugin.parser.KeyExtractorImpl

/**
 * i18n annotator for js
 */
class VueCompositeKeyAnnotator : CompositeKeyAnnotatorBase(
    FullKeyExtractor(
        VueCallContext(),
        KeyExtractorImpl()
    )
)