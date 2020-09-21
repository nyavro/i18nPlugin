package com.eny.i18n.plugin.utils.generator.code

class TsCodeGenerator: CodeGenerator {

    override fun ext(): String = "ts"

    override fun generate(key: String, index: Int): String = """
        export const test$index = (i18n: {t: Function}) => {
            return i18n.t($key);
        };
    """

    override fun generateInvalid(key: String): String = """
        const key = (s: Function) => s($key);
    """

    override fun generateNotExtracted(text: String, index: Int): String = """
        export const test$index = (i18n: {t: Function}) => {
            return $text;
        };
    """
}