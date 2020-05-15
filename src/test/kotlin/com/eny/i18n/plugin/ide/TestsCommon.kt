package com.eny.i18n.plugin.ide

import com.eny.i18n.plugin.ide.settings.Settings
import com.intellij.testFramework.fixtures.CodeInsightTestFixture
import kotlinx.coroutines.runBlocking
import kotlin.reflect.KMutableProperty

internal fun CodeInsightTestFixture.runWithSettings (settingsPack: SettingsPack, block: () -> Unit) {
    val settings = Settings.getInstance(this.project)
    val restore = settingsPack.build(settings)
    runBlocking {
        block()
    }
    restore.build(settings)
}

internal fun CodeInsightTestFixture.runVuePack (settingsPack: SettingsPack, block: () -> Unit) = runWithSettings(settingsPack.with(Settings::vue, true), block)

internal fun CodeInsightTestFixture.runVue (block: () -> Unit) = runVuePack(SettingsPack(), block)

internal class SettingsPack {
    private val values: MutableMap<KMutableProperty<*>, Any> = mutableMapOf()

    fun with(prop: KMutableProperty<*>, value: Any): SettingsPack {
        values[prop] = (value as Any)
        return this
    }

    fun build(settings: Settings):SettingsPack {
        return values.entries.fold(SettingsPack(), {
            pack, item ->
                val oldValue = item.key.getter.call(settings)
                item.key.setter.call(settings, item.value)
                pack.with(item.key, oldValue as Any)
        })
    }
}