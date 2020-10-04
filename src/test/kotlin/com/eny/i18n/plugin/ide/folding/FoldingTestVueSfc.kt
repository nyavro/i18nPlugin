package com.eny.i18n.plugin.ide.folding

import com.eny.i18n.plugin.PlatformBaseTest
import com.eny.i18n.plugin.ide.runVueConfig
import com.eny.i18n.plugin.ide.settings.Config
import com.eny.i18n.plugin.utils.generator.code.VueCodeGenerator
import com.eny.i18n.plugin.utils.generator.translation.JsonTranslationGenerator
import com.intellij.testFramework.fixtures.impl.CodeInsightTestFixtureImpl
import org.junit.jupiter.api.Test

class FoldingTestVueSfc: PlatformBaseTest() {

    override fun getTestDataPath(): String {
        return "src/test/resources/folding"
    }

    private val testConfig = Config(foldingEnabled = true)

    private val cg = VueCodeGenerator()
    private val tg = JsonTranslationGenerator()

    @Test
    fun testFolding() = myFixture.runVueConfig(testConfig) {
        val mod = "i18n"
        val f = "t"
        val translation = tg.
        myFixture.configureByText(
            "App.vue",
            cg.generateSfcBlock(
              mapOf(
                  Pair("ru", """{
                  "ref": {
                  "section": {
                  "key": "Kaj nula",
                  "longValue": "Fi tioma estiel sed. Frazo postmorgaŭ am nen, co tuje pobo mil. Jh bis geinstruisto demandosigno, posta hieraŭo pantalono al aha, du tiuj persa ili. Plu gv verbo samideano, onin fiksa piedpilko mf poa."
              }
              }
              } """),
                      Pair("en", """{
                "ref": {
                "section": {
                "key": "Translation test en",
                "longValue": "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum"
            },
                "section2": {
                "unreached": "This key is not referenced from code"
            }
            }
            }
              )      
            )
                """)),"""<p>{{ 'ref.section.key'}}</p>
                <p>{{             $f            ('ref.section.unresolved2')}}</p>
                <p>{{ $f            ('ref.section.longValue') $f            ('ref.section.key')}}</p>
                <p>{{ $f            ('ref.section.key')}}</p>"""
            )
        )
        val desc = (myFixture as CodeInsightTestFixtureImpl).getFoldingDescription(true)
        assertEquals(desc,
                """
            <template>
              <div id="app">
                <label for="locale">locale</label>
                <select v-model="locale">
                  <option>en</option>
                  <option>ja</option>
                </select>
                <p>{{ 'ref.section.key'}}</p>
                <p>{{             $f            ('ref.section.unresolved2')}}</p>
                <p>{{ <fold text='Lorem ipsum dolor si...'>            $f            ('ref.section.longValue')</fold> <fold text='Translation test en'>            $f            ('ref.section.key')</fold>}}</p>
                <p>{{ <fold text='Translation test en'>            $f            ('ref.section.key')</fold>}}</p>
              </div>
            </template>

            <i18n>
            {
              "ru": {
                "ref": {
                  "section": {
                    "key": "Kaj nula",
                    "longValue": "Fi tioma estiel sed. Frazo postmorgaŭ am nen, co tuje pobo mil. Jh bis geinstruisto demandosigno, posta hieraŭo pantalono al aha, du tiuj persa ili. Plu gv verbo samideano, onin fiksa piedpilko mf poa."
                  }
                }
              },
              "en": {
                "ref": {
                  "section": {
                    "key": "Translation test en",
                    "longValue": "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum"
                  },
                  "section2": {
                    "unreached": "This key is not referenced from code"
                  }
                }
              }
            }
            </i18n>

            <script>
            export default {
              name: 'App',
              data () { return { locale: 'en' } },
              watch: {
                locale (val) {
                  this.$mod.locale = val
                }
              }
            }
            </script>

        """.trimIndent())
    }

//    @Test
//    fun testPreferredLanguage() = myFixture.runVueConfig(
//            Config(foldingPreferredLanguage = "ru-RU", foldingMaxLength = 26, foldingEnabled = true)
//    ) {
//        myFixture.testFolding("$testDataPath/vueSfc/preferredLanguageTestVue.vue")
//    }
//
//    @Test
//    fun testPreferredLanguageInvalidConfiguration() = myFixture.runVueConfig(
//            Config(foldingPreferredLanguage = "fr", foldingEnabled = true)
//    ) {
//        myFixture.testFolding("$testDataPath/vueSfc/noFoldingVue.vue")
//    }
//
//    @Test
//    fun testIncompleteKey() = myFixture.runVueConfig(testConfig) {
//        myFixture.testFolding("$testDataPath/vueSfc/incompleteKeysVue.vue")
//    }
//
//    @Test
//    fun testFoldingDisabled() = myFixture.runVueConfig(testConfig) {
//        myFixture.testFolding("$testDataPath/vueSfc/noFoldingVue.vue")
//    }
//
//    @Test
//    fun testFoldingParametrizedTranslation() = myFixture.runVueConfig(testConfig) {
//        myFixture.testFolding("$testDataPath/vue/parametersTestVue.vue")
//    }
}

