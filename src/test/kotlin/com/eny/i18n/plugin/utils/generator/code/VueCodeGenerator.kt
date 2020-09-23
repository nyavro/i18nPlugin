package com.eny.i18n.plugin.utils.generator.code

class VueCodeGenerator: CodeGenerator {

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

    fun generateScript(text: String): String {
        val f = 't'
        return """
            <template>
                <div id="app">
                    <h1>{{ \$$f('ref.section.key') }}</h1>
                    <div>{{title}}</div>
                </div>
            </template>
            <style>
                h1 {color: #42b983;}
            </style>
            <script>
                export default {
                    name: "AppHeader",
                    computed: {
                        title() {
                            return $text;
                        }
                    }
                }
            </script>
        """
    }

    fun generateTemplate(text: String): String {
        val f = 't'
        return """
            <template>
              <div id="app">
                <div>$text</div>
                <div>{{title}}</div>
              </div>
            </template>
            <style>
              h1 {
                color: #42b983;
              }
            </style>
            <script>
              export default {
                name: "AppHeader",
                computed: {
                  title() {
                    return this.$f('tit.le');
                  }
                }
              }
            </script>
        """
    }

    override fun generateNotExtracted(text: String, index: Int): String = """
        <template>
            ${generateDivNotExtracted(text)}
        </template>
    """

    private fun generateDiv(key: String): String = "    <div>{{ \$t($key) }}</div>\n"

    private fun generateDivNotExtracted(text: String): String = "    <div>{{ \"$text\" }}</div>\n"
}