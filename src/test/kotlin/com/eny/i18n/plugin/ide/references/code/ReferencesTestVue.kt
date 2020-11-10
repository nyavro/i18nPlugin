package com.eny.i18n.plugin.ide.references.code

import com.eny.i18n.plugin.PlatformBaseTest
import com.eny.i18n.plugin.ide.VueCodeTranslationGenerators
import com.eny.i18n.plugin.ide.runVue
import com.eny.i18n.plugin.ide.runVueConfig
import com.eny.i18n.plugin.ide.settings.Config
import com.eny.i18n.plugin.utils.generator.code.CodeGenerator
import com.eny.i18n.plugin.utils.generator.translation.TranslationGenerator
import com.eny.i18n.plugin.utils.unQuote
import com.intellij.psi.PsiElement
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource

class ReferencesTestVue: PlatformBaseTest() {

    @ParameterizedTest
    @ArgumentsSource(VueCodeTranslationGenerators::class)
    fun testMultiReferenceDefNs(cg: CodeGenerator, tg: TranslationGenerator) {
        myFixture.runVue {
            myFixture.addFileToProject(
                "assets/en/translation.${tg.ext()}",
                tg.generateContent("main", "header", "title", "Welcome")
            )
            myFixture.addFileToProject(
                "assets/de/translation.${tg.ext()}",
                tg.generateContent("main", "header", "title", "Willkommen")
            )
            myFixture.configureByText("multiReference.${cg.ext()}", cg.generate("'main.header.title<caret>'"))
            read {
                val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
                assertNotNull(element)
                assertEquals(setOf("Welcome", "Willkommen"), getResolvedValues(element))
            }
        }
    }

    @ParameterizedTest
    @ArgumentsSource(VueCodeTranslationGenerators::class)
    fun testReferenceMultiDefaultNs(cg: CodeGenerator, tg: TranslationGenerator) {
        myFixture.runVueConfig(Config(defaultNs = "third,second;first ")) {
            //Resolves reference from key 'main.fruit' to three possible default ns (first,second,third):
            myFixture.addFileToProject("assets/en/first.${tg.ext()}", tg.generateContent("main", "fruit", "apple"))
            myFixture.addFileToProject("assets/en/second.${tg.ext()}", tg.generateContent("main", "fruit", "orange"))
            myFixture.addFileToProject("assets/en/third.${tg.ext()}", tg.generateContent("main", "fruit", "pear"))
            myFixture.addFileToProject("assets/de/first.${tg.ext()}", tg.generateContent("main", "fruit", "apfel"))
            myFixture.addFileToProject("assets/de/second.${tg.ext()}", tg.generateContent("main", "fruit", "apfelsine"))
            myFixture.addFileToProject("assets/de/third.${tg.ext()}", tg.generateContent("main", "fruit", "birne"))
            myFixture.configureByText("multiDefNs.${cg.ext()}", cg.generate("'main.fruit<caret>'"))
            read {
                val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
                assertNotNull(element)
                assertEquals(setOf("apfel", "apfelsine", "apple", "birne", "orange", "pear"), getResolvedValues(element))
            }
        }
    }

    @ParameterizedTest
    @ArgumentsSource(VueCodeTranslationGenerators::class)
    fun testDefaultNsReference(cg: CodeGenerator, tg: TranslationGenerator) {
        myFixture.runVue {
            myFixture.addFileToProject(
                "assets/translation.${tg.ext()}",
                tg.generateContent("ref", "section", "key", "Default ns reference"))
            myFixture.configureByText("resolved.${cg.ext()}", cg.generate("'ref.section.key<caret>'"))
            read {
                val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
                assertNotNull(element)
                assertTrue("Failed ${tg.ext()}, ${cg.ext()}", element!!.references.size > 0)
                assertEquals("Failed ${tg.ext()}, ${cg.ext()}", "Default ns reference", element.references[0].resolve()?.text?.unQuote())
            }
        }
    }

    @ParameterizedTest
    @ArgumentsSource(VueCodeTranslationGenerators::class)
    fun testRootKey(cg: CodeGenerator, tg: TranslationGenerator) {
        myFixture.runVue {
            myFixture.addFileToProject(
                "locales/en-US.${tg.ext()}",
                tg.generateContent("ref", "Reference in json"))
            myFixture.configureByText("resolved.${cg.ext()}", cg.generate("t('ref<caret>')"))
            read {
                val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
                assertNotNull(element)
                assertTrue("Failed ${tg.ext()}, ${cg.ext()}", element!!.references.size > 0)
                assertEquals("Failed ${tg.ext()}, ${cg.ext()}", "Reference in json", element.references[0].resolve()?.text?.unQuote())
            }
        }
    }

    private fun getResolvedValues(element: PsiElement?) =
            (element?.reference as I18nReference).references.map { it.reference.element?.value()?.text?.unQuote() }.toSet()
}