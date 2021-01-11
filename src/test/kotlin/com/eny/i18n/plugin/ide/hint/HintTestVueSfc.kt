package com.eny.i18n.plugin.ide.hint

import com.eny.i18n.plugin.PlatformBaseTest
import com.eny.i18n.plugin.ide.runVueConfig
import com.eny.i18n.plugin.ide.settings.VueSettings
import com.eny.i18n.plugin.utils.generator.code.VueCodeGenerator
import com.eny.i18n.plugin.utils.generator.translation.JsonTranslationGenerator
import com.intellij.codeInsight.documentation.DocumentationManager
import org.junit.Test

class HintTestVueSfc: PlatformBaseTest() {

    private val cg = VueCodeGenerator()
    private val tg = JsonTranslationGenerator()
    private val f = "\$t"
    private val translation = tg.generateNamedBlocks(
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
    )

    @Test
    fun testSingleHint() = myFixture.runVueConfig {
        myFixture.configureByText(
            "App.vue",
            cg.generateSfcBlock(
                """<p>{{ $f('ref.section.ke<caret>y')}}</p>""",
                translation
            )
        )
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