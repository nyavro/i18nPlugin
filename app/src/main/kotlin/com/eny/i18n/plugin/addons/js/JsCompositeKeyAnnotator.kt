package com.eny.i18n.plugin.addons.js

import com.eny.i18n.plugin.ide.annotator.CompositeKeyAnnotatorBase
import com.eny.i18n.plugin.key.FullKeyExtractor
import com.eny.i18n.plugin.parser.KeyExtractorImpl

/**
 * i18n annotator for js
 */
class JsCompositeKeyAnnotator : CompositeKeyAnnotatorBase(
    FullKeyExtractor(
            JsCallContext(),
        KeyExtractorImpl()
    ),
    JsLanguageFactory().translationExtractor().folderSelector()
)

