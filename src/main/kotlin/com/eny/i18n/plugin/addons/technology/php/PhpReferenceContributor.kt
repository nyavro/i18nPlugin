package com.eny.i18n.plugin.addons.technology.php

import com.eny.i18n.plugin.ide.references.code.ReferenceContributorBase

/**
 * PHP dialect reference contributor
 */
class PhpReferenceContributor: ReferenceContributorBase(PhpLanguageFactory().referenceAssistant())