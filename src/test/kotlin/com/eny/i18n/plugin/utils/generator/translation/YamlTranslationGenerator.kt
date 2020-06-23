package com.eny.i18n.plugin.utils.generator.translation

class YamlTranslationGenerator: TranslationGenerator {

    override fun ext(): String = "yml"

    override fun generateContent(root: String, key: String, value: String): String = """ 
    $root:   
        $key: $value
    subsection2: 
        key21: Ref
        key22: Etwas
    """.trimIndent()

    override fun generateContent(root: String, first: String, key: String, value: String): String = """ 
    $root:  
        $first: 
            $key: $value
        subsection2: 
            key21: Ref
            key22: Etwas
    """

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
}
