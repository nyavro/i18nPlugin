package com.eny.i18n.plugin.utils.generator.translation

class Json5TranslationGenerator: TranslationGenerator {

    override fun ext(): String = "json5"

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
}