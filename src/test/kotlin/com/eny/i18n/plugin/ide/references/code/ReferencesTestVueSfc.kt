package com.eny.i18n.plugin.ide.references.code

import com.eny.i18n.plugin.PlatformBaseTest
import com.eny.i18n.plugin.ide.runVue
import com.eny.i18n.plugin.utils.generator.code.VueCodeGenerator
import com.eny.i18n.plugin.utils.generator.translation.JsonTranslationGenerator
import com.eny.i18n.plugin.utils.unQuote
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream

/**
 * Unfortunately, references in Vue SFC components will not work.
 * Json content in Vue SFC is "third-level" language injection, it has limited support by Intellij IDEA platform.
 * https://intellij-support.jetbrains.com/hc/en-us/community/posts/360007687419-Inject-language-into-JSLiteralExpression-inside-Vue-context
 */

/**
 * Maps parsed json elements (treeElement) with XmlText elements (host)
 */
//class SfcReferenceResolver {
//
//    fun findMatch(host: PsiElement, lang: String, treeElement: PsiElement): PsiElement? {
//        return JsonPropertyWrapper(treeElement)
//                .parents()
//                .map {it.name()}
//                .fold(candidates(lang, host.children.toList())) {
//                    acc, item -> acc.mapNotNull{firstCandidate(item, it)}
//                }
//                .firstOrNull()
//                ?.drop(2)
//                ?.firstOrNull()
//    }
//
//    private fun candidates(text: String, items: List<PsiElement>): List<List<PsiElement>> {
//        val mutable = ArrayList<List<PsiElement>>()
//        var cur = ArrayList<PsiElement>()
//        items.forEach {
//            if (it.text.contains(text)) {
//                if (cur.isNotEmpty()) {
//                    mutable.add(cur.toList())
//                }
//                cur = ArrayList()
//            }
//            cur.add(it)
//        }
//        if(cur.isNotEmpty()) {
//            mutable.add(cur.toList())
//        }
//        return mutable.toList()
//    }
//
//    private fun firstCandidate(text: String, items: List<PsiElement>): List<PsiElement>? {
//        val candidates = items.dropWhile {!it.text.contains(text)}
//        return candidates.firstOrNull()?.let {listOf(it) + candidates.drop(1).takeWhile {!it.text.contains(text)}}
//    }
//}

@Disabled
class ReferencesTestVueSfc: PlatformBaseTest() {

    @ParameterizedTest
    @ArgumentsSource(SfcTestArgumentsProvider::class)
    fun testSfcReferenceResolved(vueSfcContent: String, expectedReferenceText: String) {
        myFixture.runVue() {
            myFixture.configureByText("Sfc.vue", vueSfcContent)
            read {
                val element = myFixture.file.findElementAt(myFixture.caretOffset)?.parent
                assertNotNull(element)
                assertTrue(element!!.references.size > 0)
                assertEquals(expectedReferenceText, element!!.references[0].resolve()?.text?.unQuote())
            }
        }
    }
}

class SfcTestArgumentsProvider : ArgumentsProvider {
    override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
        val tg = JsonTranslationGenerator()
        val cg = VueCodeGenerator()
        return listOf(
            Pair(
                cg.generateSfc(
                        mapOf(
                            Pair("en", tg.generateContent("key1", "value")),
                            Pair("ja", tg.generateContent("key", "こんにちは、世界！")),
                            Pair("de", tg.generateContent("key2", "Guten Tag!"))
                        ),
                        "\"<caret>key2\""
                ),
                "Guten Tag!"
            ),
            Pair(
                cg.generateSfc(
                        mapOf(
                            Pair("en", tg.generateContent("key1", "value")),
                            Pair("ja", tg.generateContent("key", "こんにちは、世界！")),
                            Pair("de", tg.generateContent("key2", "Guten Tag! Wilkommen zu unsere Platform!"))
                        ),
                        "\"<caret>key2\""
                ),
                "Guten Tag! Wilkommen zu unsere Platform!"
            ),
            Pair(
                cg.generateSfc(
                        mapOf(
                            Pair("en", tg.generate("tst3", arrayOf("key1", "value"))),
                            Pair("ja", tg.generate("tst3", arrayOf("key", "こんにちは、世界！")))
                        ),
                        "\"tst3.<caret>key\""
                ),
                "こんにちは、世界！"
            ),
            Pair(
                cg.generateSfc(
                        mapOf(
                            Pair("en", tg.generate("tst3", arrayOf("sfc", "key1", "value"))),
                            Pair("ja", tg.generate("tst3", arrayOf("sfc", "key", "こんにちは、世界！"), arrayOf("branch", "key", "こ")))
                        ),
                        "\"tst3.sfc.<caret>key\""
                ),
                "こんにちは、世界！"
            ),
            Pair(
                cg.generateSfc(
                        mapOf(
                            Pair("en", tg.generate("tst3", arrayOf("sfc", "key1", "sub", "value"))),
                            Pair("ja", tg.generate("tst3", arrayOf("sfc", "key",  "sub", "こんにちは、世界！")))
                        ),
                        "\"tst3.sfc.<caret>key.sub\""
                ),
                "こんにちは、世界！"
            )
        ).map {(vueSfcContent, result) -> Arguments.of(vueSfcContent, result)}.stream()
    }
}
