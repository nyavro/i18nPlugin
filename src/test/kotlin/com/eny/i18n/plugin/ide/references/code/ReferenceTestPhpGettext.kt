package com.eny.i18n.plugin.ide.references.code

import com.eny.i18n.plugin.PlatformBaseTest
import com.eny.i18n.plugin.ide.runWithConfig
import com.eny.i18n.plugin.ide.settings.Config
import com.eny.i18n.plugin.utils.generator.code.PhpGetTextCodeGenerator
import com.eny.i18n.plugin.utils.generator.translation.PoTranslationGenerator
import com.eny.i18n.plugin.utils.unQuote
import com.intellij.psi.PsiElement
import org.junit.Test

class ReferenceTestPhpGettext : PlatformBaseTest() {

    private val cg = PhpGetTextCodeGenerator("gettext")
    private val tg = PoTranslationGenerator()
    private val config = Config(gettext = true)

    @Test
    fun testReference() = myFixture.runWithConfig(config){
        myFixture.addFileToProject(
                "en-US/LC_MESSAGES/test.${tg.ext()}",
                tg.generateContent("ref", "section", "key", "Reference in json"))
        myFixture.configureByText("resolved.${cg.ext()}", cg.generate("'ref.section.key<caret>'"))
        val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
        assertNotNull(element)
        assertTrue("Failed ${tg.ext()}, ${cg.ext()}", element!!.references.size > 0)
        assertEquals("Failed ${tg.ext()}, ${cg.ext()}", "Reference in json", element.references[0].resolve()?.text?.unQuote())
    }

    @Test
    fun testMultiReference() = myFixture.runWithConfig(config) {
        myFixture.addFileToProject(
            "en-US/LC_MESSAGES/multi.${tg.ext()}",
            tg.generateContent("main", "header", "title", "Welcome")
        )
        myFixture.addFileToProject(
            "de-DE/LC_MESSAGES/multi.${tg.ext()}",
            tg.generateContent("main", "header", "title", "Willkommen")
        )
        myFixture.configureByText("multiReference.${cg.ext()}", cg.generate("'main.header.titl<caret>e'"))
        val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
        assertNotNull(element)
        assertEquals(setOf("Welcome", "Willkommen"), getResolvedValues(element))
    }

    @Test
    fun testMultiReferenceDefNs() = myFixture.runWithConfig(config) {
        myFixture.addFileToProject(
            "en-US/LC_MESSAGES/translation.${tg.ext()}",
            tg.generateContent("main", "header", "title", "Welcome")
        )
        myFixture.addFileToProject(
            "de-DE/LC_MESSAGES/translation.${tg.ext()}",
            tg.generateContent("main", "header", "title", "Willkommen")
        )
        myFixture.configureByText("multiReference.${cg.ext()}", cg.generate("'main.header.title<caret>'"))
        val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
        assertNotNull(element)
        assertEquals(setOf("Welcome", "Willkommen"), getResolvedValues(element))
    }

    private fun getResolvedValues(element: PsiElement?) =
        (element?.references?.get(0) as? I18nReference)?.references?.map { it.reference.element?.value()?.text?.unQuote() }?.toSet() ?: emptySet()

    @Test
    fun testInvalidTranslationRoot() = myFixture.runWithConfig(config){
        myFixture.addFileToProject(
            "de-DE/LC_MESSAGES/invalidRoot.${tg.ext()}",
            tg.generateInvalidRoot())
        myFixture.configureByText("testInvalidReference.${cg.ext()}", cg.generate("'ref.section<caret>.key'"))
        val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
        assertNotNull(element)
        assertEmpty(element!!.references)
    }

    fun testInvalidContent() = myFixture.runWithConfig(config) {
        myFixture.addFileToProject(
            "de-DE/LC_MESSAGES/invalidTranslationValue.${tg.ext()}",
            tg.generateInvalid()
        )
        myFixture.configureByText("testInvalidTranslationValue.${cg.ext()}", "'ref.section<caret>.invalid'")
        val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
        assertNotNull(element)
        assertEmpty(element!!.references)
    }

    fun testInvalidTranslationKey() {
        myFixture.addFileToProject(
            "de-DE/LC_MESSAGES/invalidTranslationValue.${tg.ext()}",
            tg.generateInvalidKey("ref.section.invalid", "value")
        )
        myFixture.configureByText("testInvalidTranslationValue.${cg.ext()}", "'ref.section<caret>.invalid'")
        val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
        assertNotNull(element)
        assertEmpty(element!!.references)
    }

    fun testInvalidTranslationValue() = myFixture.runWithConfig(config) {
        myFixture.addFileToProject(
            "de-DE/LC_MESSAGES/invalidTranslationValue.${tg.ext()}",
            tg.generateInvalidValue("ref.section.invalid")
        )
        myFixture.configureByText("testInvalidTranslationValue.${cg.ext()}", "'ref.section<caret>.invalid'")
        val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
        assertNotNull(element)
        assertEmpty(element!!.references)
    }
}
