package com.eny.i18n.plugin.utils.generator.code

class VueScriptCodeGenerator: CodeGenerator {

    override fun ext(): String = "vue"

    override fun generate(key: String, index: Int): String {
        return """
            <template>
                <div id="app"/>
            </template>
            <script>
                export default {
                    name: "AppHeader",
                    computed: {
                        title() {
                            ${generateConst(key)}
                            return "";
                        }
                    }
                }
            </script>
        """
    }

    override fun multiGenerate(vararg keys: String): String = """
        <template>
            <div id="app"/>
        </template>
        <script>
            export default {
                name: "AppHeader",
                computed: {
                    title() {
                        ${keys.map(::generateConst).joinToString("")}
                        return "";
                    }
                }
            }
        </script>
    """.trimIndent()

    override fun generateInvalid(key: String): String = """
        <script>
            export default {
                name: "AppHeader",
                computed: {
                    title() {
                        return "\${'$'}\d($key)";
                    }
                }
            }
        </script>
    """

    override fun generateBlock(text: String, index: Int): String = """
        <template>
            <div id="app"/>
        </template>
        <style>
            h1 {color: #42b983;}
        </style>
        <script>
            export default {
                name: "AppHeader",
                computed: {
                    title() {
                        const text = $text;
                        return "";
                    }
                }
            }
        </script>
    """.trimIndent()

    private fun generateConst(key: String): String = "    const text = this.\$t($key)\n"
}
