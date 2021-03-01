package com.eny.i18n.plugin.utils.generator.code

class ReactTransJsxContentGenerator: CodeGenerator {

    override fun ext(): String = "jsx"

    override fun generate(key: String, index: Int): String = "const TransComponent = () =>(<Trans i18nKey=$key>fallback</Trans>);"

    override fun generateInvalid(key: String): String = """
        const key = (t) => t(0, "$key");
    """

    override fun generateBlock(text: String, index: Int): String = """
        export const test$index = (i18n) => {
            return (<div>$text</div>);
        };
    """
}