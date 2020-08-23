package com.eny.i18n.plugin.utils.generator.code

class JsCodeGenerator: CodeGenerator {

    override fun ext(): String = "js"

    override fun generate(key: String, index: Int): String = """
        export const test$index = (i18n) => {
            return i18n.t($key);
        };
    """

    override fun generateInvalid(key: String): String = """
        const key = (t) => "$key";
    """

    override fun generateCodeForExtraction(text: String): String = """
        export const test = () => {
            const text = "$text";
        };
    """
}