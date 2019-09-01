package com.eny.i18n.plugin

/**
 * Represents i18n full key, i.e. fileName and composite key
 */
data class I18nFullKey(val fileName: String?, val compositeKey: List<String>) {

    companion object {
        const val FileNameSeparator = ":"
        const val CompositeKeySeparator = "."
        private val FullKeyPattern = "(\\w+)${FileNameSeparator}((\\w+\\${CompositeKeySeparator}?)+)".toPattern()

        /**
         * Parses string of form "SampleJsonFileName:RootKey.SubKey.Etc" to i18nFullKey
         */
        fun parse(keyLiteral: String): I18nFullKey? {
            val matcher = FullKeyPattern.matcher(keyLiteral)
            return if (matcher.matches()) {
                val fileName = matcher.group(1)
                val compositeKeyLiteral = matcher.group(2)
                I18nFullKey(fileName, parseCompositeKey(compositeKeyLiteral))
            } else null
        }

        private fun parseCompositeKey(compositeKey: String): List<String> {
            return compositeKey.split(CompositeKeySeparator).toList()
        }
    }
}