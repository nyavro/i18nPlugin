package com.eny.i18n.plugin.utils.generator.code

class VueTemplateCodeGenerator: CodeGenerator {

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

    override fun generateBlock(text: String, index: Int): String {
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

    private fun generateDiv(key: String): String = "    <div>{{ \$t($key) }}</div>\n"

    private fun generateDivNotExtracted(text: String): String = "    <div>{{ \"$text\" }}</div>\n"

    private fun message(key: String): String {
        val f = "t"
        return "<p>message: {{ $f($key) }}</p>"
    }

    fun generateSfc(translationMap: Map<String, String>, vararg key: String): String {
        val ns = "\$i18n"
        return """
           <template>
              <div id="app">
                <label for="locale">locale</label>
                <select v-model="locale">
                  <option>en</option>
                  <option>ja</option>
                </select>
                ${key.map {message(it)}.joinToString("\n")}         
              </div>
            </template>
            
            <i18n>
            {
              ${translationContent(translationMap)}
            }
            </i18n>
            
            <script>
            export default {
              name: 'App',
              data () { return { locale: 'en' } },
              watch: {
                locale (val) {
                  this.$ns.locale = val
                }
              }
            }
            </script>
        """
    }

    private fun translationContent(translationMap: Map<String, String>) =
        translationMap.map {
            "\"${it.key}\": ${it.value}"
        }.joinToString(",")
}
