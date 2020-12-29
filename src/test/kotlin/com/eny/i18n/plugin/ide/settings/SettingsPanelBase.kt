package com.eny.i18n.plugin.ide.settings

import io.mockk.unmockkAll
import net.sourceforge.marathon.javadriver.JavaDriver
import net.sourceforge.marathon.javadriver.JavaProfile
import org.junit.After
import org.junit.Before
import javax.swing.JFrame
import javax.swing.SwingUtilities
import kotlin.reflect.KMutableProperty1
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

open class SettingsPanelBase <T> (val formConstructor: (T) -> SettingsFormFragment<T>, val constructor: () -> T) {

    protected lateinit var driver: JavaDriver

    @Before
    fun setUp() {
        driver = JavaDriver(JavaProfile(JavaProfile.LaunchMode.EMBEDDED))
    }

    protected fun runWithSettings(settings: T, block: (settings: T) -> Unit) {
        val frame = JFrame()
        frame.contentPane.add(formConstructor(settings).getRootPanel())
        SwingUtilities.invokeLater {
            frame.isVisible = true
        }
        block(settings)
        frame.dispose()
    }

    fun checkStringProperty(keys: String, message: String, property: KMutableProperty1<T, String>) = runWithSettings(constructor()) {
        settings ->
            val cb = driver.findElementByName(message)
            assertNotNull(cb)
            val value = cb.text
            assertEquals(value, property.get(settings))
            cb.clear()
            cb.sendKeys(keys)
            assertEquals(keys, property.get(settings))
    }

    fun checkBooleanProperty(message: String, property: KMutableProperty1<T, Boolean>) = runWithSettings(constructor()) {
        settings ->
            val cb = driver.findElementByName(message)
            assertNotNull(cb)
            val value = cb.text == "true"
            assertEquals(value, property.get(settings))
            cb.click()
            assertEquals(!value, property.get(settings))
    }

    @After
    fun tearDown() {
        driver.quit()
        unmockkAll()
    }
}