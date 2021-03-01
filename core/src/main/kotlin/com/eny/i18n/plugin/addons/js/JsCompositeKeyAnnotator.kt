package com.eny.i18n.plugin.addons.js

import com.eny.i18n.plugin.ide.CompositeKeyAnnotatorBase
import com.eny.i18n.plugin.ide.JsCallContext
import com.eny.i18n.plugin.ide.JsLanguageFactory
import com.eny.i18n.plugin.ide.annotator.*

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

