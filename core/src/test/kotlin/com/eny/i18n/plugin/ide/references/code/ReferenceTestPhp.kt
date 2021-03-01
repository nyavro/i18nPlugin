package com.eny.i18n.plugin.ide.references.code

import com.eny.i18n.plugin.PlatformBaseTest
//import com.eny.i18n.plugin.ide.PhpCodeAndTranslationGenerators
import com.eny.i18n.plugin.utils.generator.code.PhpCodeGenerator
import com.eny.i18n.plugin.utils.unQuote
import com.intellij.psi.PsiElement
import org.junit.Test

//import org.junit.jupiter.params.ParameterizedTest
//import org.junit.jupiter.params.provider.ArgumentsSource

// @TODO 14

class ReferenceTestPhp : PlatformBaseTest() {

    private val cg = PhpCodeGenerator()


    @Test
    fun testStub1() {
        assertTrue(true)
    }

//    @ParameterizedTest
//    @ArgumentsSource(PhpCodeAndTranslationGenerators::class)
//    fun testReference(cg: CodeGenerator, tg: TranslationGenerator) {
//        myFixture.addFileToProject(
//            "assets/test.${tg.ext()}",
//            tg.generateContent("ref", "section", "key", "Reference in json"))
//        myFixture.configureByText("resolved.${cg.ext()}", cg.generate("'test:ref.section.key<caret>'"))
//        read {
//            val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
//            assertNotNull(element)
//            assertTrue("Failed ${tg.ext()}, ${cg.ext()}", element!!.references.size > 0)
//            assertEquals("Failed ${tg.ext()}, ${cg.ext()}", "Reference in json", element.references[0].resolve()?.text?.unQuote())
//        }
//    }

//    @ParameterizedTest
//    @ArgumentsSource(PhpCodeAndTranslationGenerators::class)
//    fun testMultiReference(cg: CodeGenerator, tg:TranslationGenerator) {
//        myFixture.addFileToProject(
//            "assets/en/multi.${tg.ext()}",
//            tg.generateContent("main", "header", "title", "Welcome")
//        )
//        myFixture.addFileToProject(
//            "assets/de/multi.${tg.ext()}",
//            tg.generateContent("main", "header", "title", "Willkommen")
//        )
//        myFixture.configureByText("multiReference.${cg.ext()}", cg.generate("'multi:main.header.titl<caret>e'"))
//        read {
//            val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
//            assertNotNull(element)
//            assertEquals(setOf("Welcome", "Willkommen"), getResolvedValues(element))
//        }
//    }

//    @ParameterizedTest
//    @ArgumentsSource(PhpCodeAndTranslationGenerators::class)
//    fun testMultiReferenceDefNs(cg: CodeGenerator, tg:TranslationGenerator) {
//        myFixture.addFileToProject(
//            "assets/en/translation.${tg.ext()}",
//            tg.generateContent("main", "header", "title", "Welcome")
//        )
//        myFixture.addFileToProject(
//            "assets/de/translation.${tg.ext()}",
//            tg.generateContent("main", "header", "title", "Willkommen")
//        )
//        myFixture.configureByText("multiReference.${cg.ext()}", cg.generate("'main.header.title<caret>'"))
//        read {
//            val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
//            assertNotNull(element)
//            assertEquals(setOf("Welcome", "Willkommen"), getResolvedValues(element))
//        }
//    }
//
//    @ParameterizedTest
//    @ArgumentsSource(PhpCodeAndTranslationGenerators::class)
//    fun testReferenceMultiDefaultNs(cg: CodeGenerator, tg:TranslationGenerator) {
//        myFixture.runI18nConfig(Pair(I18NextSettings::defaultNs, "third,second;first ")) {
//            Resolves reference from key 'main.fruit' to three possible default ns (first,second,third):
//            myFixture.addFileToProject("assets/en/first.${tg.ext()}", tg.generateContent("main", "fruit", "apple"))
//            myFixture.addFileToProject("assets/en/second.${tg.ext()}", tg.generateContent("main", "fruit", "orange"))
//            myFixture.addFileToProject("assets/en/third.${tg.ext()}", tg.generateContent("main", "fruit", "pear"))
//            myFixture.addFileToProject("assets/de/first.${tg.ext()}", tg.generateContent("main", "fruit", "apfel"))
//            myFixture.addFileToProject("assets/de/second.${tg.ext()}", tg.generateContent("main", "fruit", "apfelsine"))
//            myFixture.addFileToProject("assets/de/third.${tg.ext()}", tg.generateContent("main", "fruit", "birne"))
//            myFixture.configureByText("multiDefNs.${cg.ext()}", cg.generate("\"main.fruit<caret>\""))
//            read {
//                val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
//                assertNotNull(element)
//                assertEquals(setOf("apfel", "apfelsine", "apple", "birne", "orange", "pear"), getResolvedValues(element))
//            }
//        }
//    }

    private fun getResolvedValues(element: PsiElement?) =
        (element?.references?.get(0) as? I18nReference)?.references?.map { it.reference.element?.value()?.text?.unQuote() }?.toSet() ?: emptySet()

//    @ParameterizedTest
//    @ArgumentsSource(PhpCodeAndTranslationGenerators::class)
//    fun testDefaultNsReference(cg: CodeGenerator, tg:TranslationGenerator) {
//        myFixture.addFileToProject(
//            "assets/translation.${tg.ext()}",
//            tg.generateContent("ref", "section", "key", "Default ns reference"))
//        myFixture.configureByText("resolved.${cg.ext()}", cg.generate("'ref.section.key<caret>'"))
//        read {
//            val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
//            assertNotNull(element)
//            assertTrue("Failed ${tg.ext()}, ${cg.ext()}", element!!.references.size > 0)
//            assertEquals("Failed ${tg.ext()}, ${cg.ext()}", "Default ns reference", element.references[0].resolve()?.text?.unQuote())
//        }
//    }

//    @ParameterizedTest
//    @ArgumentsSource(PhpCodeAndTranslationGenerators::class)
//    fun testPartiallyResolvedReference(cg: CodeGenerator, tg:TranslationGenerator) {
//        myFixture.addFileToProject(
//            "assets/test.${tg.ext()}",
//            tg.generateContent("ref", "section", "key", "Default ns reference"))
//        myFixture.configureByText("testPartiallyResolvedReference.${cg.ext()}", cg.generate("'test:ref.section<caret>.not.found'"))
//        read {
//            val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
//            assertNotNull(element)
//            assertTrue(element!!.references.size > 0)
//            assertEquals("section", element.references[0].resolve()?.text?.unQuote())
//        }
//    }

//    @ParameterizedTest
//    @ArgumentsSource(PhpCodeAndTranslationGenerators::class)
//    fun testInvalidTranslationRoot(cg: CodeGenerator, tg:TranslationGenerator) {
//        myFixture.addFileToProject(
//            "assets/invalidRoot.${tg.ext()}",
//            tg.generateInvalidRoot())
//        myFixture.configureByText("testInvalidReference.${cg.ext()}", cg.generate("`invalidRoot:ref.section<caret>.\${b ? 'key' : 'key2'}`"))
//        read {
//            val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
//            assertNotNull(element)
//            assertEmpty(element!!.references)
//        }
//    }

//    fun testInvalidTranslationValue() {
//        myFixture.addFileToProject(
//                "assets/invalidTranslationValue.${tg.ext()}",
//                tg.generateContent("ref", "section", "key", "value"))
//        myFixture.configureByText("testInvalidTranslationValue.${cg.ext()}", "'invalidTranslationValue:ref.section<caret>.invalid'")
//        val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
//        assertNotNull(element)
//        assertEquals("section", element!!.references[0].resolve()?.text?.unQuote())
//    }

//    @ParameterizedTest
//    @ArgumentsSource(PhpCodeAndTranslationGenerators::class)
//    fun testRootKeyDefNs(cg: CodeGenerator, tg:TranslationGenerator) {
//        myFixture.addFileToProject(
//            "assets/translation.${tg.ext()}",
//            tg.generateContent("ref", "Reference in json"))
//        myFixture.configureByText("resolved.${cg.ext()}", cg.generate("'ref<caret>'"))
//        read {
//            val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
//            assertNotNull(element)
//            assertTrue("Failed ${tg.ext()}, ${cg.ext()}", element!!.references.size > 0)
//            assertEquals("Failed ${tg.ext()}, ${cg.ext()}", "Reference in json", element.references[0].resolve()?.text?.unQuote())
//        }
//    }

//    @ParameterizedTest
//    @ArgumentsSource(PhpCodeAndTranslationGenerators::class)
//    fun testRootKey(cg: CodeGenerator, tg:TranslationGenerator) {
//        myFixture.addFileToProject(
//            "assets/test.${tg.ext()}",
//            tg.generateContent("ref", "Reference in json"))
//        myFixture.configureByText("resolved.${cg.ext()}", cg.generate("'test:ref<caret>'"))
//        read {
//            val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
//            assertNotNull(element)
//            assertTrue("Failed ${tg.ext()}, ${cg.ext()}", element!!.references.size > 0)
//            assertEquals("Failed ${tg.ext()}, ${cg.ext()}", "Reference in json", element.references[0].resolve()?.text?.unQuote())
//        }
//    }
}
