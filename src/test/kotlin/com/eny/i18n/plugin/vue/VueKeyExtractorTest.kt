package com.eny.i18n.plugin.vue

import org.junit.Assert.assertEquals
import org.junit.Test

class VueKeyExtractorTest {

    @Test
    fun extractOneParameterTemplate() {
        val templateText = "{{ \$t('pages.blog.subtitle') }}"
        val keyExtractor = VueKeyExtractor()
        assertEquals("pages.blog.subtitle", keyExtractor.extract(templateText))
    }

    @Test
    fun extractOneParameterTemplate2() {
        val templateText = "{{ \$t(\"upload.tooltip.05\") }}"
        val keyExtractor = VueKeyExtractor()
        assertEquals("upload.tooltip.05", keyExtractor.extract(templateText))
    }

    @Test
    fun extractOneParameterTemplate3() {
        val templateText = "{{ \$t('upload.tooltip.05') }}"
        val keyExtractor = VueKeyExtractor()
        assertEquals("upload.tooltip.05", keyExtractor.extract(templateText))
    }

    @Test
    fun extractTwoParameterTemplate() {
        val templateText = "{{ \$t('pages.blog.subtitle', { count: posts.length }) }}"
        val keyExtractor = VueKeyExtractor()
        assertEquals("pages.blog.subtitle", keyExtractor.extract(templateText))
    }
}