package com.eny.i18n.plugin.utils.generator.code

class TsxCodeGenerator: CodeGenerator {

    override fun ext(): String = "tsx"

    override fun generate(key: String, index: Int): String = """
        export const test$index = (i18n: {t: Function}) => {
            return (<div>{i18n.t($key)}</div>);
        };
    """

    override fun generateInvalid(key: String): String = """
        const key = () => (<div>{"$key"}</div>);
    """
}