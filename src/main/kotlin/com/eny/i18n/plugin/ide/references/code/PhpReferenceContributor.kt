package com.eny.i18n.plugin.ide.references.code

import com.eny.i18n.plugin.language.php.PhpLanguageFactory
import com.eny.i18n.plugin.language.php.PhpReferenceAssistant

/**
 * PHP dialect reference contributor
 */
class PhpReferenceContributor: ReferenceContributorBase(PhpLanguageFactory().referenceAssistant())