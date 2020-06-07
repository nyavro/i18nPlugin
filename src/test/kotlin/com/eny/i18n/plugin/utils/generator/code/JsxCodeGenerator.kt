package com.eny.i18n.plugin.utils.generator.code

class JsxCodeGenerator: CodeGenerator {

    override fun ext(): String = "jsx"

    override fun generate(key: String, index: Int): String = """
        export const test{$index} = (i18n) => {
            return (<div>{i18n.t($key)}</div>);
        };
    """
}