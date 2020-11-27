package com.eny.i18n.plugin.ide.annotator

import com.eny.i18n.plugin.language.js.JsCallContext
import com.eny.i18n.plugin.key.FullKeyExtractor
import com.eny.i18n.plugin.language.js.JsLanguageFactory
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