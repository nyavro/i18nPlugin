package com.eny.i18n.plugin.utils.generator.code

class VueTsCodeGenerator: CodeGenerator {

    override fun ext(): String = "vue"

    override fun generate(key: String, index: Int): String {
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
            <script lang="ts">
                export default {
                    name: "AppHeader",
                    computed: {
                        title(): string {
                            const expr: string = 'ts here';
                            return $key;
                        }
                    }
                }
            </script>
        """
    }

    override fun generateInvalid(key: String): String = """
        <template>
            <h1>{{\$\d($key)}}</h1>
        </template>
    """

    override fun generateBlock(text: String, index: Int): String {
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
            <script lang="ts">
                export default {
                    name: "AppHeader",
                    computed: {
                        title(): string {
                            const expr: string = 'ts here';
                            return $text;
                        }
                    }
                }
            </script>
        """
    }
}
