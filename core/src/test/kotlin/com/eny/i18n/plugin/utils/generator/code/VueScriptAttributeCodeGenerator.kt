package com.eny.i18n.plugin.utils.generator.code

class VueScriptAttributeCodeGenerator(private val attributeName: String): CodeGenerator {
    private val f = 't'
    override fun ext(): String = "vue"

    override fun generate(key: String, index: Int): String = """
        <template>
            <comp $attributeName=$key/>
        </template>
    """

    override fun multiGenerate(vararg keys: String): String = """
        <template>
            ${keys.map(::generateDiv).joinToString("")}
        </template>
    """

    override fun generateInvalid(key: String): String = """
        <template>
            <h1>{{\$\d($key)}}</h1>
        </template>
    """

    override fun generateBlock(text: String, index: Int): String = """
        <template>
            ${generateDivNotExtracted(text)}
        </template>
    """

    private fun generateDiv(key: String): String = "    <div>{{ \$t($key) }}</div>\n"

    private fun generateDivNotExtracted(text: String): String = "    <div>{{ \"$text\" }}</div>\n"
}
