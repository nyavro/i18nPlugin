package com.eny.i18n.plugin.utils.generator.code

class JsxCodeGenerator: CodeGenerator {

    override fun extension(): String = "jsx"

    override fun generate(key: String, index: Int): String = """
        export const test{$index} = () => {
            return (<div>{i18n.t('$key')}</div>);
        };
    """
}