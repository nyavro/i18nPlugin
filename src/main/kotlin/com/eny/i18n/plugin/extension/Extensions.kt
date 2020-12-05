package com.eny.i18n.plugin.extension

import com.eny.i18n.plugin.factory.ContentGenerator
import com.intellij.openapi.extensions.ExtensionPointName

class Extensions {

    companion object {
        val CONTENT_GENERATORS: ExtensionPointName<ContentGenerator> = ExtensionPointName.create("com.eny.i18n.contentGenerator")
    }
}