package com.eny.i18n.plugin.ide.references

import com.eny.i18n.plugin.utils.unQuote
import com.intellij.psi.PsiElement
import com.intellij.testFramework.fixtures.BasePlatformTestCase

abstract class ReferencesTestBase(private val ext: String) : BasePlatformTestCase() {

    override fun getTestDataPath(): String = "src/test/resources/references"

    fun testReference() {
        myFixture.configureByFiles("jsx/testReference.jsx", "assets/test.$ext")
        val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
        assertNotNull(element)
        assertEquals("Reference in json", element!!.references[0].resolve()?.text?.unQuote())
    }

    fun testMultiReference() {
        myFixture.configureByFiles("jsx/multiReference.jsx", "assets/en/multi.$ext", "assets/de/multi.$ext")
        val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
        assertNotNull(element)
        assertEquals(setOf("Welcome", "Willkommen"), getResolvedValues(element))
    }

    fun testMultiReferenceDefNs() {
        myFixture.configureByFiles("jsx/defNsmultiReference.jsx", "assets/en/translation.$ext", "assets/de/translation.$ext")
        val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
        assertNotNull(element)
        assertEquals(setOf("Welcome", "Willkommen"), getResolvedValues(element))
    }

    private fun getResolvedValues(element: PsiElement?) =
        (element?.reference as I18nReference).references.map { it.element?.value()?.text?.unQuote() }.toSet()

    fun testDefaultNsReference() {
        myFixture.configureByFiles("jsx/defNsReference.jsx", "assets/translation.$ext")
        val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
        assertNotNull(element)
        assertEquals("Default ns reference", element!!.references[0].resolve()?.text?.unQuote())
    }

    fun testPartiallyResolvedReference() {
        myFixture.configureByFiles("tsx/testPartiallyResolvedReference.tsx", "assets/test.$ext")
        val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
        assertNotNull(element)
        assertEquals("section", element!!.references[0].resolve()?.text?.unQuote())
    }

    fun testExpressionReference() {
        myFixture.configureByFiles("tsx/testReference.tsx", "assets/test.$ext")
        val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
        assertNotNull(element)
        assertEquals("section", element!!.references[0].resolve()?.text?.unQuote())
    }

    fun testInvalidTranslationRoot() {
        myFixture.configureByFiles("tsx/testInvalidReference.tsx", "assets/invalidRoot.$ext")
        val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
        assertNotNull(element)
        assertEmpty(element!!.references)
    }

    fun testInvalidTranslationValue() {
        myFixture.configureByFiles("tsx/testInvalidTranslationValue.tsx", "assets/invalidTranslationValue.$ext")
        val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
        assertNotNull(element)
        assertEquals("section", element!!.references[0].resolve()?.text?.unQuote())
    }
}

class ReferencesToJsonTranslationTest: ReferencesTestBase("json")
class ReferencesToYamlTranslationTest: ReferencesTestBase("yml")