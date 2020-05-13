package com.eny.i18n.plugin.ide

import com.eny.i18n.plugin.ide.settings.Settings
import com.intellij.testFramework.fixtures.IdeaProjectTestFixture
import kotlin.reflect.KMutableProperty

internal fun IdeaProjectTestFixture.runVue (block: () -> Unit) {
    val settings = Settings.getInstance(this.project)
    settings.vue = true
    block()
    settings.vue = false
}

internal fun IdeaProjectTestFixture.runVuePack (settingsPack: SettingsPack, block: () -> Unit) {
    val settings = Settings.getInstance(this.project)
    val restore = settingsPack.with(Settings::vue, true).build(settings)
    block()
    restore.build(settings)
}

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