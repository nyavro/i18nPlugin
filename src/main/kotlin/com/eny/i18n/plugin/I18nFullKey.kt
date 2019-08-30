package com.eny.i18n.plugin

/**
 * Represents i18n full key, i.e. fileName and composite key
 */
data class I18nFullKey(val fileName: String?, val compositeKey: List<String>) {

    companion object {
        const val FileNameSeparator = ":"
        const val CompositeKeySeparator = "."

        /**
         * Parses string of form "SampleJsonFileName:RootKey.SubKey.Etc" to i18nFullKey
         */
        fun parse(keyLiteral: String): I18nFullKey? {
            return if (keyLiteral.contains(FileNameSeparator)) {
                val components = keyLiteral.split(FileNameSeparator).toList()
                if (components.size == 2) {
                    val fileName = components[0]
                    val compositeKeyLiteral = components[1]
                    return I18nFullKey(fileName, parseCompositeKey(compositeKeyLiteral))
                } else {
                    null    //Expecting only one filename separator in full key literal
                }
            } else {
                null        //For now supports full file description only, i.e. "SampleJsonFileName:RootKey.SubKey.Etc"
            }
        }

        private fun parseCompositeKey(compositeKey: String): List<String> {
            return compositeKey.split(CompositeKeySeparator).toList()
        }
    }
}