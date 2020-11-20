package com.eny.i18n.plugin.ide.folding

import com.eny.i18n.plugin.PlatformBaseTest
import com.eny.i18n.plugin.ide.runVueConfig
import com.eny.i18n.plugin.ide.settings.Config
import com.eny.i18n.plugin.utils.generator.code.VueSfcCodeGenerator
import com.eny.i18n.plugin.utils.generator.translation.JsonTranslationGenerator
import com.intellij.testFramework.fixtures.impl.CodeInsightTestFixtureImpl
import org.junit.jupiter.api.Test

class FoldingTestVueSfc: PlatformBaseTest() {

    private val testConfig = Config(foldingEnabled = true)
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
                "ref",
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
    private val cg = VueSfcCodeGenerator(translation, false)
    private val cgCheck = VueSfcCodeGenerator(translation, true)

    @Test
    fun testFolding() = myFixture.runVueConfig(testConfig) {
        myFixture.configureByText(
            "App.vue",
            cg.generateBlock(
                """
                <p>{{ 'ref.section.key'}}</p>
                <p>{{ $f('ref.section.unresolved2')}}</p>
                <p>{{ $f('ref.section.longValue') $f('ref.section.key')}}</p>
                <p>{{ $f('ref.section.key')}}</p>"""
            )
        )
        assertEquals(
            cgCheck.generateBlock(
                """
                <p>{{ 'ref.section.key'}}</p>
                <p>{{ $f('ref.section.unresolved2')}}</p>
                <p>{{ <fold text='Lorem ipsum dolor si...'>$f('ref.section.longValue')</fold> <fold text='Translation test en'>$f('ref.section.key')</fold>}}</p>
                <p>{{ <fold text='Translation test en'>$f('ref.section.key')</fold>}}</p>"""
            ),
            (myFixture as CodeInsightTestFixtureImpl).getFoldingDescription(false)
        )
    }

    @Test
    fun testPreferredLanguage() = myFixture.runVueConfig(
        Config(foldingPreferredLanguage = "ru", foldingMaxLength = 26, foldingEnabled = true)
    ) {
        myFixture.configureByText(
            "App.vue",
            cg.generateBlock(
                """
                    <h1>{{ $f('ref.section.longValue')}}</h1>
                    <h1>{{ $f('ref.section.unresolved2')}}</h1>
                    <h1>{{ $f('ref.section.key')}}</h1>"""
            )
        )
        assertEquals(
            cgCheck.generateBlock(
                """
                    <h1>{{ <fold text='Fi tioma estiel sed. Frazo...'>$f('ref.section.longValue')</fold>}}</h1>
                    <h1>{{ $f('ref.section.unresolved2')}}</h1>
                    <h1>{{ <fold text='Kaj nula'>$f('ref.section.key')</fold>}}</h1>"""
            ),
            (myFixture as CodeInsightTestFixtureImpl).getFoldingDescription(false)
        )
    }

    @Test
    fun testPreferredLanguageInvalidConfiguration() = myFixture.runVueConfig(
            Config(foldingPreferredLanguage = "fr", foldingEnabled = true)
    ) {
        myFixture.configureByText(
            "App.vue",
            cg.generateBlock(
                """
                    <h1>{{ $f('test:ref.section.longValue')}}</h1>
                    <h1>{{ $f('test:ref.section.unresolved2')}}</h1>
                    <h1>{{ $f('test:ref.section.key')}}</h1>"""
            )
        )
        assertEquals(
            cgCheck.generateBlock(
                """
                    <h1>{{ $f('test:ref.section.longValue')}}</h1>
                    <h1>{{ $f('test:ref.section.unresolved2')}}</h1>
                    <h1>{{ $f('test:ref.section.key')}}</h1>"""
            ),
            (myFixture as CodeInsightTestFixtureImpl).getFoldingDescription(false)
        )
    }

    @Test
    fun testIncompleteKey() = myFixture.runVueConfig(testConfig) {
        myFixture.configureByText(
            "App.vue",
            cg.generateBlock(
                """
                    <h1>{{ $f('def.section')+$f('def.section2')}}</h1>
                    <h1>{{ $f('def.section')}}</h1>"""
            )
        )
        assertEquals(
            cgCheck.generateBlock(
                """
                    <h1>{{ $f('def.section')+$f('def.section2')}}</h1>
                    <h1>{{ $f('def.section')}}</h1>"""
            ),
            (myFixture as CodeInsightTestFixtureImpl).getFoldingDescription(false)
        )
    }

    @Test
    fun testFoldingDisabled() = myFixture.runVueConfig(testConfig) {
        myFixture.configureByText(
            "App.vue",
            cg.generateBlock(
                """
                    <h1>{{ $f('test:ref.section.longValue')}}</h1>
                    <h1>{{ $f('test:ref.section.unresolved2')}}</h1>
                    <h1>{{ $f('test:ref.section.key')}}</h1>"""
            )
        )
        assertEquals(
            cgCheck.generateBlock(
                """
                    <h1>{{ $f('test:ref.section.longValue')}}</h1>
                    <h1>{{ $f('test:ref.section.unresolved2')}}</h1>
                    <h1>{{ $f('test:ref.section.key')}}</h1>"""
            ),
            (myFixture as CodeInsightTestFixtureImpl).getFoldingDescription(false)
        )
    }

    @Test
    fun testFoldingParametrizedTranslation() = myFixture.runVueConfig(testConfig) {
        myFixture.configureByText(
            "App.vue",
            cg.generateBlock("<p>{{ $f('ref.section.longValue', { language: \$i18n.locale }) }}</p>")
        )
        assertEquals(
            cgCheck.generateBlock(
                "<p>{{ <fold text='Lorem ipsum dolor si...'>$f('ref.section.longValue', { language: \$i18n.locale })</fold> }}</p>"
            ),
            (myFixture as CodeInsightTestFixtureImpl).getFoldingDescription(false)
        )
    }
}


