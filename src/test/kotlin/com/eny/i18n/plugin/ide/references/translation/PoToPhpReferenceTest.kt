package com.eny.i18n.plugin.ide.references.translation

import com.eny.i18n.plugin.PlatformBaseTest
import com.eny.i18n.plugin.utils.at
import com.eny.i18n.plugin.utils.generator.code.PhpGetTextCodeGenerator
import com.eny.i18n.plugin.utils.generator.translation.PoTranslationGenerator
import org.junit.Test

class PoToPhpReferenceTest: PlatformBaseTest() {

    private val cg = PhpGetTextCodeGenerator("gettext")
    private val tg = PoTranslationGenerator()

    @Test
    fun testStub1() {
        assertTrue(true)
    }

    fun disabledTranslationToCodeReference() {
        val translation = tg.generateContent("ref", "section<caret>", "key1", "val 1")
        myFixture.configureByText(
            "test.php",
            cg.multiGenerate(
                "'skip.ref.section.key1'",
                "'ref.section.key1'",
                "'drop.ref.section.key3'",
                "'skpref.section.key1'",
                "'ref.section.key1'"
            )
        )
//        myFixture.configureByText("file.po", translation)
        myFixture.configureFromExistingVirtualFile(
            myFixture.addFileToProject("en-US/LC_MESSAGES/messages.${tg.ext()}", translation).virtualFile
        )
        val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
        val ref = element?.references?.at(0)
        assertTrue(ref is TranslationToCodeReference)
        assertEquals(
            setOf("'ref.section.key1'"),
            (ref as TranslationToCodeReference).findRefs().map { it.text }.toSet()
        )
    }
}