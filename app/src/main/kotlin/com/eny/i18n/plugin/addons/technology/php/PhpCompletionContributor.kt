package com.eny.i18n.plugin.addons.technology.php

import com.eny.i18n.plugin.ide.completion.CompositeKeyCompletionContributor

/**
 * PHP Completion contributor
 */
class PhpCompletionContributor: CompositeKeyCompletionContributor(PhpLanguageFactory().callContext())