package com.eny.i18n.plugin.yaml

import com.eny.i18n.plugin.utils.generator.translation.TranslationGenerator

class YamlTranslationGenerator: TranslationGenerator {

    override fun ext(): String = "yml"

    override fun generateContent(key: String, value: String): String = """
    $key: $value
    subsection2: 
        key21: Ref
        key22: Etwas
    """.trimIndent()

    override fun generateContent(root: String, key: String, value: String): String = """ 
    $root:   
        $key: $value
    """

    override fun generateContent(root: String, first: String, key: String, value: String): String = """$root: 
  $first: 
    $key: $value"""

    override fun generateContent(root: String, first: String, second: String, key: String, value: String): String = """ 
    $root: 
        $first: 
            $second: 
                $key: $value
            subsection2: 
                key21: Ref
                key22: Etwas
    """

    override fun generatePlural(root: String, first: String, key: String, value1: String, value2: String, value5: String): String = """
    $root: 
        $first: 
            $key-1: $value1
            $key-2: $value2
            $key-5: $value5
        subsection2: 
            key21: Ref
            key22: Etwas 
    """

    override fun generateInvalid(): String = """
        ref:
            section:
                key:
                    invalid
    """.trimIndent()

    override fun generateInvalidRoot(): String = ""

    private fun generateBranchByList(list: List<String>): String {
        val keyValue = list.takeLast(2)
        return list
            .dropLast(2)
            .foldRight(Pair("${"   ".repeat(list.size-2)}  ${keyValue[0]}: ${keyValue[1]}", 0)) {
                item, acc ->
                val tabs = "   ".repeat(list.size-acc.second-3)
                Pair("$tabs  ${item}: \n${acc.first}${tabs}#$tabs", acc.second+1)
            }.first
    }

    private fun generateBranchByList2(list: List<String>): String {
        val (key, value) = list.takeLast(2)
        return list
            .dropLast(2)
            .foldRight(Pair("${key}: ${value}", 0)) {
                item, (first, second) ->
                val tabs = "   ".repeat(list.size-second-3)
                Pair("$tabs  ${item}: \n${first}${tabs}#$tabs", second+1)
            }.first
    }

    override fun generate(root: String, vararg branches: Array<String>): String {
        return "$root:\n${generate(*branches)}"
    }

    override fun generate(vararg branches: Array<String>): String {
        return branches.map{generateBranchByList(it.toList())}.joinToString("\n")
    }

    override fun generateNamedBlock(key: String, block: String, level: Int): String = """ 
    ${"\t".repeat(level)}$key: $block 
    """

    override fun generateNamedBlocks(vararg blocks: Pair<String, String>): String =
        blocks.map{(name, block) -> formatBlock(name, block)}.joinToString("\n")

    private fun formatBlock(name: String, block: String): String = "\t$name: $block"
}
