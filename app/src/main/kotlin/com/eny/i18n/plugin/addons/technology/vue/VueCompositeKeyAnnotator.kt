package com.eny.i18n.plugin.addons.technology.vue

import com.eny.i18n.plugin.ide.annotator.CompositeKeyAnnotatorBase
import com.eny.i18n.plugin.ide.annotator.FullKeyExtractor
import com.eny.i18n.plugin.ide.annotator.KeyExtractorImpl

/**
 * i18n annotator for js
 */
class VueCompositeKeyAnnotator : CompositeKeyAnnotatorBase(
        FullKeyExtractor(
                VueCallContext(),
                KeyExtractorImpl()
        ),
    VueLanguageFactory().translationExtractor().folderSelector()
)