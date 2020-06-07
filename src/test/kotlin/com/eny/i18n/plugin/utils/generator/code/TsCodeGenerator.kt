package com.eny.i18n.plugin.utils.generator.code

class TsCodeGenerator: CodeGenerator {

    override fun extension(): String = "ts"

    override fun generate(key: String, index: Int): String = """
        export const test{$index} = (i18n: {t: Function}) => {
            return i18n.t($key);
        };
    """
}