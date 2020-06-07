package com.eny.i18n.plugin.utils.generator.translation

class YamlTranslationGenerator: TranslationGenerator {

    override fun ext(): String = "yml"

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
}
