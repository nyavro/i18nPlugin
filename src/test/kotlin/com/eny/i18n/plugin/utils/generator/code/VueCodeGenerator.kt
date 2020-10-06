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

    fun generateSfcBlock(codeBlock: String, translationBlock: String, folding: Boolean = false): String {
        val ns = "\$i18n"
        val foldStart = if (folding) "<fold text='...'>" else ""
        val foldStartScript = if (folding) "<fold text='{...}'>" else ""
        val foldEnd = if (folding) "</fold>" else ""
        val foldApp = if (folding) "<fold text='{name: 'App'...}'>" else ""
        return """<template${foldStart}>
  <div id="app"${foldStart}>
    <label for="locale">locale</label>
    <select v-model="locale"${foldStart}>
      <option>en</option>
      <option>ja</option>
    </select${foldEnd}>
    ${codeBlock}
  </div${foldEnd}>
</template${foldEnd}>
<i18n${foldStart}>
${translationBlock}
</i18n${foldEnd}>
<script${foldStart}>
export default ${foldApp}{
  name: 'App',
  data () { return { locale: 'en' } },
  watch: ${foldStartScript}{
    locale (val) ${foldStartScript}{
      this.$ns.locale = val
    }${foldEnd}
  }${foldEnd}
}${foldEnd}
</script${foldEnd}>"""
    }

    private fun translationContent(translationMap: Map<String, String>) =
        translationMap.map {
            "\"${it.key}\": ${it.value}"
        }.joinToString(",")
}
