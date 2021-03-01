package com.eny.i18n.plugin.utils.generator.translation

class Json5TranslationGenerator: TranslationGenerator {

    override fun ext(): String = "json5"

    override fun generateContent(key: String, value: String): String = """
    { 
        $key: '$value'  
    }   
    """.trimIndent()

    override fun generateContent(root: String, key: String, value: String): String = """
    {
        $root: { 
            "$key": '$value' 
        }
    }   
    """.trimIndent()

    override fun generateContent(root: String, first: String, key: String, value: String): String = """
    {
        "$root": {
            $first: {
                '$key': "$value"
            },
            "plurals": {
                "value-1": "tt",
                "value-2": "qq",
                "value-5": "vv"
            }
        }
    }        
    """

    override fun generateContent(root: String, first: String, second: String, key: String, value: String): String = """
    {
        '$root': {
            "$first": {
                $second: {
                    "key11": "ref11",
                    '$key': "$value"
                },
                "subsection2": {
                    "key21": "Ref",
                    "key22": "Etwas"
                }
            }
        }
    } 
    """

    override fun generatePlural(root: String, first: String, key: String, value1: String, value2: String, value5: String): String = """
    {
        $root: {
            '$first': {
                "$key-1": "$value1"
                "$key-2": "$value2"
                '$key-5': '$value5'
            },
            "second": {
                "value": "tt"
            }
        }
    }        
    """

    override fun generateInvalid(): String = """
        {
          ref: {
            "section": {
              key: 'Reference in json',
              "invalid":
            }
          }
        }
    """.trimIndent()

    override fun generateInvalidRoot(): String = ""

    private fun generateBranchByList(list: List<String>): String {
        val keyValue = list.takeLast(2)
        return list
                .dropLast(2)
                .foldRight(Pair("${"\t".repeat(list.size)}\"${keyValue[0]}\": \"${keyValue[1]}\"\n", 0)) {
                    item, acc ->
                    val tabs = "\t".repeat(list.size-acc.second-1)
                    Pair("$tabs\"${item}\": {\n${acc.first}${tabs}\n$tabs}", acc.second+1)
                }.first
    }

    private fun generateBranchByList2(list: List<String>): String {
        val keyValue = list.takeLast(2)
        return list
            .dropLast(2)
            .foldRight(Pair("${"\t".repeat(list.size)}\"${keyValue[0]}\": \"${keyValue[1]}\"\n", 0)) {
                item, acc ->
                val tabs = "\t".repeat(list.size-acc.second-1)
                Pair("$tabs\"${item}\": {\n${acc.first}${tabs}\n$tabs}", acc.second+1)
            }.first
    }

    override fun generate(root: String, vararg branches: Array<String>): String =
        "{\n   \"$root\": ${generate(*branches)}}"

    override fun generate(vararg branches: Array<String>): String =
        "{\n${branches.map{generateBranchByList(it.toList())}.joinToString(",\n")}\n   }"

    override fun generateNamedBlock(key: String, block: String, level: Int): String =
        """{
            "$key": $block
        }
        """

    override fun generateNamedBlocks(vararg blocks: Pair<String, String>): String =
        """{
            ${blocks.map{(name, block) -> formatBlock(name, block)}.joinToString(",\n")}
        }"""

    private fun formatBlock(name: String, block: String): String = """"$name": $block"""
}
