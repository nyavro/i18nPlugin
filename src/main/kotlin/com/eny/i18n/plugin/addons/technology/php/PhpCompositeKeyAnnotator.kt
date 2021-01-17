package com.eny.i18n.plugin.addons.technology.php

import com.eny.i18n.plugin.key.FullKeyExtractor
import com.eny.i18n.plugin.ide.annotator.CompositeKeyAnnotatorBase
import com.eny.i18n.plugin.parser.KeyExtractorImpl

/**
 * i18n annotator for php
 */
class PhpCompositeKeyAnnotator: CompositeKeyAnnotatorBase(
    FullKeyExtractor(
            PhpCallContext(),
        KeyExtractorImpl()
    ),
    PhpLanguageFactory().translationExtractor().folderSelector()
)