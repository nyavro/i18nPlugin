package com.eny.i18n.plugin.utils.generator.code

class PhpGetTextCodeGenerator(private val translationFunction: String): CodeGenerator {

    override fun ext(): String = "php"

    override fun generate(key: String, index: Int): String = """
        <?php
            ${generateLine(key)} 
    """

    override fun multiGenerate(vararg keys: String): String = """
        <?php
            ${keys.map(::generateLine).joinToString("\\n")}
    """

    override fun generateInvalid(key: String): String = """
        <?php
            echo invld($key);
    """.trimIndent()

    override fun generateBlock(text: String, index: Int): String = """
        <?php
            ${generateLineNotExtracted(text)} 
    """

    private fun generateLine(key: String): String = "    echo $translationFunction($key);"
    private fun generateLineNotExtracted(text: String): String = "    echo '$text';"
}