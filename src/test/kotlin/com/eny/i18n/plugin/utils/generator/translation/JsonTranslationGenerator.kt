package com.eny.i18n.plugin.utils.generator.translation

class JsonTranslationGenerator: TranslationGenerator {

    override fun extension(): String = "json"

    override fun generateContent(root: String, first: String, key: String, value: String): String = """
        {
            "$root": {
                "$first": {
                    "$key": "$value"
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
            "$root": {
                "$first": {
                    "$second": {
                        "key11": "ref11",
                        "$key": "$value"
                    },
                    "subsection2": {
                        "key21": "Ref",
                        "key22": "Etwas"
                    }
                }
            }
        } 
    """
}