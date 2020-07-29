package com.eny.i18n.plugin.ide.completion

import com.eny.i18n.plugin.language.php.PhpCallContext

/**
 * PHP Completion contributor
 */
class PhpCompletionContributor: CompositeKeyCompletionContributor(PhpCallContext())