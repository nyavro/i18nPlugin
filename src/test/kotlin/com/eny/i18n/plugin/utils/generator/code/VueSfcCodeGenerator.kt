package com.eny.i18n.plugin.utils.generator.code

class VueSfcCodeGenerator(private val translationBlock: String, private val folding: Boolean = false): CodeGenerator {

    override fun ext(): String = "vue"

    override fun generate(key: String, index: Int): String = """
        <template>
            ${generateDiv(key)}
        </template>
    """

    override fun multiGenerate(vararg keys: String): String = """
        <template>
            ${keys.map(::generateDiv).joinToString("")}
        </template>
    """

    override fun generateInvalid(key: String): String = """
        <template>
            <h1>{{\$\d($key)}}</h1>
        </template>
    """

    override fun generateBlock(text: String, index: Int): String {
        val ns = "\$i18n"
        val foldStart = if (folding) "<fold text='...'>" else ""
        val foldStartScript = if (folding) "<fold text='{...}'>" else ""
        val foldEnd = if (folding) "</fold>" else ""
        val foldApp = if (folding) "<fold text='{name: 'App'...}'>" else ""
        return """
            <template${foldStart}>
                <div id="app"${foldStart}>
                    <label for="locale">locale</label>
                    <select v-model="locale"${foldStart}>
                        <option>en</option>
                        <option>ja</option>
                    </select${foldEnd}>
                    ${text}
                </div${foldEnd}>
            </template${foldEnd}>
            <i18n${foldStart}>
                ${translationBlock}
            </i18n${foldEnd}>
            <script${foldStart}>
                export default ${foldApp}{
                    name: 'App', data () { return { locale: 'en' } },
                    watch: ${foldStartScript}{
                        locale (val)${foldStartScript}{
                            this.$ns.locale = val
                        }${foldEnd}
                    }${foldEnd}
                }${foldEnd}
            </script ${foldEnd}>
        """.trimIndent()
    }

    private fun generateDiv(key: String): String = "    <div>{{ \$t($key) }}</div>\n"
}
