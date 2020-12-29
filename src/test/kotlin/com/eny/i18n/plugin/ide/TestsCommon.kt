package com.eny.i18n.plugin.ide

import com.eny.i18n.plugin.ide.settings.CommonSettings
import com.eny.i18n.plugin.ide.settings.I18NextSettings
import com.eny.i18n.plugin.ide.settings.PoSettings
import com.eny.i18n.plugin.ide.settings.VueSettings
import com.intellij.testFramework.fixtures.CodeInsightTestFixture
import kotlin.jvm.internal.MutablePropertyReference
import kotlin.reflect.KMutableProperty1

private fun <T, S: Any> CodeInsightTestFixture.runWithSettings(props: Map<KMutableProperty1<T, S>, S>, actual: T, block: () -> Unit) {
    val stash = props.mapValues {
        it.key.get(actual)
    }
    props.forEach {
        it.key.set(actual, it.value)
    }
    block()
    stash.forEach {
        it.key.set(actual, it.value)
    }
}

internal fun <S: Any> CodeInsightTestFixture.runCommonConfig (vararg props: Pair<KMutableProperty1<CommonSettings, S>, S>, block: () -> Unit) =
        runWithSettings(mapOf(*props), CommonSettings.getInstance(this.project), block)

internal fun <S: Any> CodeInsightTestFixture.runI18nConfig (vararg props: Pair<KMutableProperty1<I18NextSettings, S>, S>, block: () -> Unit) =
        runWithSettings(mapOf(*props), I18NextSettings.getInstance(this.project), block)

internal fun <S: Any> CodeInsightTestFixture.runVueConfig (vararg props: Pair<KMutableProperty1<VueSettings, S>, S>, block: () -> Unit) =
        runWithSettings(mapOf(*props), VueSettings.getInstance(this.project), block)

internal fun <S: Any> CodeInsightTestFixture.runPoConfig (vararg props: Pair<KMutableProperty1<PoSettings, S>, S>, block: () -> Unit) =
        runWithSettings(mapOf(*props), PoSettings.getInstance(this.project), block)