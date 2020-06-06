package com.eny.i18n.plugin.utils.generator.code

class VueCodeGenerator: CodeGenerator {

    override fun extension(): String = "vue"

    override fun generate(key: String, index: Int): String = """
        <template>
            ${generateDiv(key)}
        </template>
    """

    override fun multiGenerate(vararg keys: String): String = """
        <template>
            ${keys.map(::generateDiv).joinToString("")}
        </template>
    """

    private fun generateDiv(key: String): String = "    <div>{{ \$t($key) }}</div>\n"
}