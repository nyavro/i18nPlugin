package com.eny.i18n.plugin.parser

import com.eny.i18n.plugin.ide.settings.Settings
import com.eny.i18n.plugin.key.FullKey
import com.eny.i18n.plugin.key.parser.KeyParserBuilder
import com.intellij.openapi.project.Project

class RawKeyParser(private val project: Project) {
    fun parse(rawKey: RawKey): FullKey? {
        val config = Settings.getInstance(project).config()
        val parser = (
                if (config.gettext)
                    KeyParserBuilder.withoutTokenizer()
                else
                    KeyParserBuilder
                        .withSeparators(config.nsSeparator, config.keySeparator)
                        .withDummyNormalizer()
                        .withTemplateNormalizer()
                ).build()
        return parser.parse(rawKey, config.gettext, config.firstComponentNs)
    }
}