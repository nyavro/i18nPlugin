package com.eny.i18n.plugin.ide.annotator

import com.eny.i18n.plugin.language.php.PhpCallContext
import com.eny.i18n.plugin.key.FullKeyExtractor
import com.eny.i18n.plugin.language.php.PhpLanguageFactory
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