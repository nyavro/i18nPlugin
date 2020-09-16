package com.eny.i18n.plugin.parser

import com.eny.i18n.plugin.utils.KeyElement
import com.intellij.codeInsight.completion.CompletionInitializationContext
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class KeyNormalizerTest {

//valid:ROOT.Key1.---idea-placeholder-here---
    @Test
    fun normalizeDummy() {
        val elements = listOf(
            KeyElement.literal("valid:ROOT.Key1.${CompletionInitializationContext.DUMMY_IDENTIFIER}")
        )
        val normalized = KeyNormalizerImpl().normalize(elements)
        assertEquals(1, normalized.size)
        assertEquals(
            "valid:ROOT.Key1.${CompletionInitializationContext.DUMMY_IDENTIFIER_TRIMMED}",
            normalized.get(0).text
        )
    }
}
