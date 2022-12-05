package com.eny.i18n.extensions.localization.yaml

import com.eny.i18n.plugin.ide.references.translation.TranslationToCodeReference
import com.eny.i18n.plugin.utils.generator.code.JsCodeGenerator
import com.eny.i18n.plugin.utils.generator.translation.YamlTranslationGenerator
import com.eny.i18n.plugin.utils.unQuote
import com.intellij.testFramework.fixtures.BasePlatformTestCase


class YamlTranslationToCodeTest: BasePlatformTestCase() {

    fun testSingleReference() {
        val cg = JsCodeGenerator()
        val key = "'test:ref.section.key0'"
        myFixture.configureByText("testReference0.${cg.ext()}", cg.generate(key))
        val tg = YamlTranslationGenerator()
        myFixture.configureByText(
            "test.${tg.ext()}",
            tg.generateContent("ref", "section", "key<caret>0", "Translation at ref section key")
        )
        val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
        assertNotNull(element)
        assertEquals(key.unQuote(), element!!.references[0].resolve()?.text?.unQuote())
    }

    fun testInvalidTranslation() {
        val tg = YamlTranslationGenerator()
        myFixture.configureByText("invalid.${tg.ext()}", "item<caret> text")
        val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
        assertNotNull(element)
        assertTrue(element!!.references.isEmpty())
    }

    fun testNoReference() {
        val tg = YamlTranslationGenerator()
        myFixture.configureByText(
                "test.${tg.ext()}",
            tg.generateContent("ref", "section", "key<caret>", "Translation at ref section key")
        )
        val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
        assertNotNull(element)
        assertTrue(element!!.references.isEmpty())
    }

    fun testMultipleReferences() {
        val cg = JsCodeGenerator()
            val key = "'multiTest:ref.section.subsection1.key1'"
            myFixture.configureByText(
                "multiReference1.${cg.ext()}",
                cg.multiGenerate(
                    key,
                    "skip-multiTest:ref.section.subsection1.key1",
                    key
                )
            )
            myFixture.configureByText(
                "multiReference2.${cg.ext()}",
                cg.multiGenerate(
                    key,
                    "skip-multiTest2:ref.section.subsection1.key1"
                )
            )
        val tg = YamlTranslationGenerator()
            myFixture.configureByText(
                "multiTest.${tg.ext()}",
                tg.generateContent("ref", "section", "subsection1", "key<caret>1", "Translation")
            )
            val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
            val msg = cg.ext() + "," + tg.ext()
            val ref = element!!.references[0] as? TranslationToCodeReference
            if (ref == null) {
                fail(msg)
            } else {
                assertEquals(msg, setOf(key.unQuote()), ref.findRefs().mapNotNull {it.text?.unQuote()}.toSet())
            }
        }

    fun testObjectReference() {
        val cg = JsCodeGenerator()
        val tg = YamlTranslationGenerator()
        myFixture.configureByText(
            "testReference.${cg.ext()}",
            cg.multiGenerate(
                "'skip-objectRef:ref.section.key1'",
                "'objectRef0:ref.section.key2'",
                "`skip-objectRef:ref.section.\${key2}`",
                "'objectRef0:ref.section.key1'",
                "'skip-objectRef:ref.section.key2'",
                "`objectRef0:ref.section.\${key2}`"
            )
        )
        myFixture.configureByText(
            "objectRef0.${tg.ext()}",
            tg.generateContent("ref", "section<caret>", "key1", "val 1")
        )
        val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
        val ref = element!!.references[0]
        assertTrue(ref is TranslationToCodeReference)
        val refs = (ref as TranslationToCodeReference).findRefs().map { item -> item.text }.toSet()
        assertEquals(
            cg.ext() + "," + tg.ext(),
            setOf("'objectRef0:ref.section.key1'", "'objectRef0:ref.section.key2'", "`objectRef0:ref.section.\${key2}`"),
            refs
        )
    }

    fun testInvalidRange() {
        val tg = YamlTranslationGenerator()
        val cg = JsCodeGenerator()
            myFixture.configureByText(
                "testReference.${cg.ext()}",
                cg.multiGenerate(
                    "'skip-objectRef:ref.section.key1'",
                    "'objectRef0:ref.section.key2'",
                    "`skip-objectRef:ref.section.\${key2}`",
                    "'objectRef0:ref.section.key1'",
                    "'skip-objectRef:ref.section.key2'",
                    "`objectRef0:ref.section.\${key2}`"
                )
            )
            myFixture.configureByText(
                "objectRef0.${tg.ext()}",
                "{<caret>\""
            )
            val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
            assertNotNull(element)
            assertTrue(element!!.references.isEmpty())
    }

    fun testClickOnValue() {
        val tg = YamlTranslationGenerator()
            myFixture.configureByText(
                "testValue.${tg.ext()}",
                tg.generateContent("ref", "section", "key", "transla<caret>tion")
            )
            val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
            assertNotNull(element)
            assertTrue(element!!.references.isEmpty())
    }

    fun testSingleReferenceQuoted() {
        val tg = YamlTranslationGenerator()
        val cg = JsCodeGenerator()
            val key = "'testQuoted:ref.section.key0'"
            myFixture.configureByText(
                "testReferenceQuoted.${cg.ext()}",
                cg.generate(key)
            )
            myFixture.configureByText(
                "testQuoted.${tg.ext()}",
                tg.generateContent("ref", "section", "\"key<caret>0\"", "Reference in yaml")
            )
            val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
            assertNotNull(element)
            assertEquals(key.unQuote(), element!!.references[0].resolve()?.text?.unQuote())
    }
}
