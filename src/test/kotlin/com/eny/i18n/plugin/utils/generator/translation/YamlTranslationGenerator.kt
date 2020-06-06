package com.eny.i18n.plugin.utils.generator.translation

class YamlTranslationGenerator: TranslationGenerator {

    override fun extension(): String = "yml"

    override fun generateContent(root: String, first: String, key: String, value: String): String = """ 
        $root:  
            $first: 
                key11: ref11
                $key: $value
            subsection2: 
                key21: Ref
                key22: Etwas
    """

    override fun generateContent(root: String, first: String, second: String, key: String, value: String): String = """ 
        $root: 
            $first: 
                $second: 
                    key11: ref11
                    $key: $value
                subsection2: 
                    key21: Ref
                    key22: Etwas
    """
}
