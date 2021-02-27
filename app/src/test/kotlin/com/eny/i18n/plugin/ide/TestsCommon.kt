package com.eny.i18n.plugin.ide

import com.eny.i18n.plugin.ide.annotator.CommonSettings
import com.eny.i18n.plugin.ide.settings.I18NextSettings
import com.eny.i18n.plugin.addons.technology.po.PoSettings
import com.eny.i18n.plugin.addons.technology.vue.VueSettings
import com.intellij.testFramework.fixtures.CodeInsightTestFixture
import kotlin.reflect.KMutableProperty1

fun <T> runWithBooleanSettings(props: Map<KMutableProperty1<T, Boolean>, Boolean>, actual: T, block: (T) -> Unit) {
    val stash = props.mapValues { it.key.get(actual) }
    props.forEach { it.key.set(actual, it.value) }
    block(actual)
    stash.forEach { it.key.set(actual, it.value) }
}

private fun <T> runWithStringSettings(props: Map<KMutableProperty1<T, String>, String>, actual: T, block: (T) -> Unit) {
    val stash = props.mapValues { it.key.get(actual) }
    props.forEach { it.key.set(actual, it.value) }
    block(actual)
    stash.forEach { it.key.set(actual, it.value) }
}

private fun <T> runWithIntSettings(props: Map<KMutableProperty1<T, Int>, Int>, actual: T, block: (T) -> Unit) {
    val stash = props.mapValues { it.key.get(actual) }
    props.forEach { it.key.set(actual, it.value) }
    block(actual)
    stash.forEach { it.key.set(actual, it.value) }
}

internal fun CodeInsightTestFixture.runCommonConfig (
        strProp: Pair<KMutableProperty1<CommonSettings, String>, String>,
        booleanProp: Pair<KMutableProperty1<CommonSettings, Boolean>, Boolean>,
        block: (CommonSettings) -> Unit) =
        runWithStringSettings(mapOf(strProp), CommonSettings.getInstance(this.project)) {
            runWithBooleanSettings(mapOf(booleanProp), it, block)
        }

internal fun CodeInsightTestFixture.runCommonConfig (
        strProp: Pair<KMutableProperty1<CommonSettings, String>, String>,
        booleanProp: Pair<KMutableProperty1<CommonSettings, Boolean>, Boolean>,
        numberProp: Pair<KMutableProperty1<CommonSettings, Int>, Int>,
        block: (CommonSettings) -> Unit) =
        runWithStringSettings(mapOf(strProp), CommonSettings.getInstance(this.project)) {
            settings -> runWithBooleanSettings(mapOf(booleanProp), settings) {
                runWithIntSettings(mapOf(numberProp), it, block)
            }
        }

internal fun CodeInsightTestFixture.runCommonConfig (
        booleanProp: Pair<KMutableProperty1<CommonSettings, Boolean>, Boolean>,
        block: (CommonSettings) -> Unit) = runWithBooleanSettings(mapOf(booleanProp), CommonSettings.getInstance(this.project), block)

internal fun CodeInsightTestFixture.runCommonConfigStr (
        strProp: Pair<KMutableProperty1<CommonSettings, String>, String>,
        block: (CommonSettings) -> Unit) = runWithStringSettings(mapOf(strProp), CommonSettings.getInstance(this.project), block)

internal fun CodeInsightTestFixture.runI18nConfig (vararg props: Pair<KMutableProperty1<I18NextSettings, String>, String>, block: (I18NextSettings) -> Unit) =
        runWithStringSettings(mapOf(*props), I18NextSettings.getInstance(this.project), block)

internal fun CodeInsightTestFixture.runVueConfig (vararg props: Pair<KMutableProperty1<VueSettings, String>, String>, block: (VueSettings) -> Unit) =
        runWithStringSettings(mapOf(*props), VueSettings.getInstance(this.project)) {
            settings -> runWithBooleanSettings(mapOf(Pair(VueSettings::vue, true)), settings, block)
        }
internal fun CodeInsightTestFixture.runPoConfig (vararg props: Pair<KMutableProperty1<PoSettings, *>, *>, block: (PoSettings) -> Unit) =
        runWithBooleanSettings(mapOf(Pair(PoSettings::gettext, true)), PoSettings.getInstance(this.project), block)