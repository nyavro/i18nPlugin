package com.eny.i18n.plugin.parser

import com.eny.i18n.plugin.utils.KeyElement
import com.intellij.codeInsight.completion.CompletionInitializationContext
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class KeyNormalizerTest {

//valid:ROOT.Key1.---idea-placeholder-here---
    @Test
    fun normalizeDummy() {
        val element = KeyElement.literal("valid:ROOT.Key1.${CompletionInitializationContext.DUMMY_IDENTIFIER}")
        val normalized = DummyTextNormalizer().normalize(element)
        assertEquals(
            "valid:ROOT.Key1.${CompletionInitializationContext.DUMMY_IDENTIFIER_TRIMMED}",
            normalized?.text
        )
    }
}
