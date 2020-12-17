package com.eny.i18n.plugin.ide.hint

import com.eny.i18n.plugin.PlatformBaseTest
import com.eny.i18n.plugin.ide.runVue
import com.eny.i18n.plugin.utils.generator.code.VueCodeGenerator
import com.eny.i18n.plugin.utils.generator.code.VueSfcCodeGenerator
import com.eny.i18n.plugin.utils.generator.translation.JsonTranslationGenerator
import com.intellij.codeInsight.documentation.DocumentationManager
import org.junit.jupiter.api.Test

class HintTestVueSfc: PlatformBaseTest() {

    private val tg = JsonTranslationGenerator()
    private val f = "\$t"
    private val cg = VueSfcCodeGenerator(
        tg.generateNamedBlocks(
            Pair(
                "ru",
                tg.generateNamedBlock(
                    "ref",
                    tg.generateNamedBlock(
                        "section",
                        tg.generate(
                            arrayOf("key", "Kaj nula"),
                            arrayOf("longValue", "Fi tioma estiel sed. Frazo postmorgaŭ am nen, co tuje pobo mil. Jh bis geinstruisto demandosigno, posta hieraŭo pantalono al aha, du tiuj persa ili. Plu gv verbo samideano, onin fiksa piedpilko mf poa.")
                        )
                    )
                )
            ),
            Pair(
                "en",
                tg.generateNamedBlock(
                    "ref1",
                    tg.generateNamedBlocks(
                        Pair(
                            "section",
                            tg.generate(
                                arrayOf("key", "Translation test en"),
                                arrayOf("longValue", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum")
                            )
                        ),
                        Pair(
                            "section2",
                            tg.generate(
                                arrayOf("unreached", "This key is not referenced from code")
                            )
                        )
                    )
                )
            )
        ),
        false
    )

    @Test
    fun testSingleHint() = myFixture.runVue {
        myFixture.configureByText(
            "App.vue",
            cg.generateBlock(
                """<p>{{ $f('ref.section.ke<caret>y')}}</p>"""
            )
        )
        read {
            val codeElement = myFixture.file.findElementAt(myFixture.caretOffset)
            //Not supported for Vue SFC:
            assertNull(
                DocumentationManager
                    .getProviderFromElement(codeElement)
                    .getQuickNavigateInfo(
                        myFixture.elementAtCaret,
                        codeElement
                    )
            )
        }
    }
}