package com.eny.i18n.plugin.utils.generator.translation

class PoTranslationGenerator: TranslationGenerator {

    override fun ext(): String = "po"

    override fun generateContent(key: String, value: String): String = """
        msgid "$key"
        msgstr "$value"
    """.trimIndent()

    private fun merge(vararg arg: String):String = arg.toList().filterNot {it.isBlank()}.joinToString(".")

    override fun generateContent(root: String, key: String, value: String): String = generateContent(
        merge(root, key), value
    )

    override fun generateContent(root: String, first: String, key: String, value: String): String = generateContent(
        merge(root, first, key), value
    )

    override fun generateContent(root: String, first: String, second: String, key: String, value: String): String = generateContent(
        merge(root, first, second, key), value
    )

    override fun generatePlural(root: String, first: String, key: String, value1: String, value2: String, value5: String): String {
        val merged = merge(root, first, key)
        return """
            msgid "$merged"
            msgid_plural "more"
            msgstr[0] "$value1"
            msgstr[1] "$value2"
            msgstr[2] "$value5"
        """.trimIndent()
    }

    override fun generateInvalid(): String = """
        msgid inv
        msgstr moreinv
    """.trimIndent()

    override fun generateInvalidRoot(): String = ""

    override fun generate(root: String, vararg branches: Array<String>): String {
        TODO("Not yet implemented")
    }

    override fun generate(vararg branches: Array<String>): String {
        TODO("Not yet implemented")
    }

    override fun generateNamedBlock(key: String, block: String, level: Int): String {
        TODO("Not yet implemented")
    }

    override fun generateNamedBlocks(vararg blocks: Pair<String, String>): String {
        TODO("Not yet implemented")
    }
}
