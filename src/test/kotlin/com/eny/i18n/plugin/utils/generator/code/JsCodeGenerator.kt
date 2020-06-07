package com.eny.i18n.plugin.utils.generator.code

class JsCodeGenerator: CodeGenerator {

    override fun extension(): String = "js"

    override fun generate(key: String, index: Int): String = """
        export const test{$index} = (i18n) => {
            return i18n.t($key);
        };
    """
}