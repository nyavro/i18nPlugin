package com.eny.i18n.plugin.ide.quickfix

import com.intellij.json.psi.JsonElementGenerator
import com.intellij.json.psi.JsonObject
import com.intellij.json.psi.JsonPsiUtil
import com.intellij.psi.PsiElement
import org.jetbrains.yaml.YAMLElementGenerator
import org.jetbrains.yaml.psi.YAMLMapping

/**
 * Generates psi content
 */
interface PsiContentGenerator {

    /**
     * Checks if current generator is suitable for content generation in given node
     *
     * @param {PsiElement} element to check suitability
     */
    fun isSuitable(element: PsiElement): Boolean

    /**
     * Generates translation entry
     *
     * @param {PsiElement} parent element in translation file
     * @param {String} key Translation key
     * @param {String} value Translation value
     */
    fun generateTranslationEntry(element: PsiElement, key: String, value: String)
}

/**
 * Generates yaml specific psi content
 */
class YamlPsiContentGenerator : PsiContentGenerator {
    override fun isSuitable(element: PsiElement): Boolean = element is YAMLMapping

    override fun generateTranslationEntry(element: PsiElement, key: String, value: String) {
        val generator = YAMLElementGenerator.getInstance(element.project)
        element.add(generator.createEol())
        element.add(generator.createYamlKeyValue(key, value))
    }
}

/**
 * Generates json specific psi content
 */
class JsonPsiContentGenerator : PsiContentGenerator {
    override fun isSuitable(element: PsiElement): Boolean {
        return element is JsonObject
    }

    override fun generateTranslationEntry(element: PsiElement, key: String, value: String) {
        JsonPsiUtil
            .addProperty(
                element as JsonObject,
                JsonElementGenerator(element.project).createProperty(key, value),
                false
            )
    }
}