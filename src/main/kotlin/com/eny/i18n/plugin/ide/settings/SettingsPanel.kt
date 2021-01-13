package com.eny.i18n.plugin.ide.settings

import com.eny.i18n.plugin.utils.PluginBundle
import com.jgoodies.forms.factories.DefaultComponentFactory
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Dimension
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import java.lang.reflect.Modifier
import javax.swing.*
import javax.swing.border.CompoundBorder
import javax.swing.border.EmptyBorder
import javax.swing.border.LineBorder
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener
import javax.swing.text.JTextComponent
import kotlin.reflect.KMutableProperty0

fun addLimitationsAndHandlers(component: JTextComponent, maxLength: Int, onChange: (newText: String) -> Unit = {}, isValid: (e: Char) -> Boolean = { true }) {
    component.addKeyListener(object: KeyAdapter() {
        override fun keyTyped(e: KeyEvent) {
            if (component.text.length - (if (component.selectedText==null) 0 else component.selectedText.length) >= maxLength || !isValid(e.keyChar)) {
                e.consume()
            }
        }
    })
    component.getDocument().addDocumentListener(object : DocumentListener {
        override fun changedUpdate(e: DocumentEvent?) = onChange(component.text)
        override fun insertUpdate(e: DocumentEvent?) = onChange(component.text)
        override fun removeUpdate(e: DocumentEvent?) = onChange(component.text)
    })
}

class FormUtils {

    fun checkbox(label:String, property: KMutableProperty0<Boolean>): JPanel {
        val panel = JPanel()
        panel.preferredSize = Dimension(350, 30)
        panel.layout = BorderLayout()
        val checkbox = JCheckBox(label, property.get())
        checkbox.name = label
        checkbox.addItemListener { _ -> property.set(checkbox.isSelected) }
        panel.add(checkbox, BorderLayout.WEST)
        return panel
    }

    fun textInput(label:String, property: KMutableProperty0<String>):JPanel {
        val panel = JPanel()
        panel.layout = BorderLayout()
        panel.preferredSize = Dimension(350, 30)
        panel.add(JLabel(label), BorderLayout.WEST)
        val control = JTextField(property.get())
        control.name = label
        addLimitationsAndHandlers(control, 100, property::set)
        control.preferredSize = Dimension(100, 30)
        panel.add(control, BorderLayout.EAST)
        return panel
    }

    fun numberInput(label:String, property: KMutableProperty0<Int>):JPanel {
        val panel = JPanel()
        panel.layout = BorderLayout()
        panel.preferredSize = Dimension(350, 30)
        panel.add(JLabel(label), BorderLayout.WEST)
        val control = JTextField(property.get().toString())
        control.name = label
        addLimitationsAndHandlers(control, 2, {property.set(it.toInt())}, {('0'..'9').contains(it)})
        panel.add(control, BorderLayout.EAST)
        control.preferredSize = Dimension(100, 30)
        return panel
    }

    fun textArea(label:String, property: KMutableProperty0<String>):JPanel {
        val panel = JPanel()
        panel.layout = BorderLayout()
        val labelPanel = JPanel()
        labelPanel.layout = BorderLayout()
        labelPanel.add(JLabel(label), BorderLayout.PAGE_START)
        panel.add(labelPanel, BorderLayout.WEST)
        val control = JTextArea(property.get())
        control.name = label
        addLimitationsAndHandlers(control, 1000, property::set)
        control.lineWrap = true
        control.wrapStyleWord = true
        control.isEditable = true
        control.columns = 20
        control.setBorder(CompoundBorder(LineBorder(Color.LIGHT_GRAY), EmptyBorder(1, 3, 1, 1)))
        panel.add(control, BorderLayout.EAST)
        return panel
    }
}

class ModificationCheck<T : Any> {
    fun hash(settings: T): String {
        return settings
            .javaClass
            .declaredFields
            .filter { !Modifier.isStatic(it.modifiers)}
            .map {it.setAccessible(true); it.name + ":" + it.get(settings)}
            .joinToString()
    }
}
/**
 * Settings configuration panel
 */
class CommonSettingsFormFragment(val settings: CommonSettings): SettingsFormFragment<CommonSettings> {

    private val formUtils = FormUtils()

    private val modificationCheck = ModificationCheck<CommonSettings>()

    private val initialHash = modificationCheck.hash(settings)

    /**
     * Returns Settings main panel
     */
    override fun getRootPanel(): JPanel {
        val root = JPanel()
        root.layout = BorderLayout()
        //TODO move to bundle
        root.add(DefaultComponentFactory.getInstance().createSeparator("Settings"), BorderLayout.NORTH)
        root.add(settingsPanel(), BorderLayout.WEST)
        return root
    }

    private fun settingsPanel(): JPanel {
        val panel = JPanel()
        panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)
        panel.add(formUtils.checkbox(PluginBundle.getMessage("settings.search.in.project.files.only"), settings::searchInProjectOnly))
        panel.add(formUtils.checkbox(PluginBundle.getMessage("settings.folding.enabled"), settings::foldingEnabled))
        panel.add(formUtils.textInput(PluginBundle.getMessage("settings.folding.preferredLanguage"), settings::foldingPreferredLanguage))
        panel.add(formUtils.numberInput(PluginBundle.getMessage("settings.folding.maxLength"), settings::foldingMaxLength))
        panel.add(formUtils.checkbox(PluginBundle.getMessage("settings.extraction.sorted"), settings::extractSorted))
        panel.add(formUtils.checkbox(PluginBundle.getMessage("settings.annotations.partially.translated.enabled"), settings::partialTranslationInspectionEnabled))
        return panel
    }

    override fun isModified(): Boolean = initialHash == modificationCheck.hash(settings)
}

/**
 * Settings configuration panel
 */
class I18NextSettingsFormFragment(val settings: I18NextSettings): SettingsFormFragment<I18NextSettings> {

    private val formUtils = FormUtils()

    private val modificationCheck = ModificationCheck<I18NextSettings>()

    private val initialHash = modificationCheck.hash(settings)

    override fun isModified(): Boolean = initialHash == modificationCheck.hash(settings)

    /**
     * Returns Settings main panel
     */
    override fun getRootPanel(): JPanel {
        val root = JPanel()
        root.layout = BorderLayout()
        root.add(DefaultComponentFactory.getInstance().createSeparator("I18Next Settings"), BorderLayout.NORTH)
        root.add(settingsPanel(), BorderLayout.WEST)
        return root
    }

    private fun separator(label:String, property: KMutableProperty0<String>):JPanel {
        val panel = JPanel()
        panel.layout = BorderLayout()
        panel.preferredSize = Dimension(350, 30)
        panel.add(JLabel(label), BorderLayout.WEST)
        val control = JTextField(property.get())
        control.name = label
        addLimitationsAndHandlers(control, 1, property::set, {!" {}$`".contains(it)})
        control.preferredSize = Dimension(30, 30)
        panel.add(control, BorderLayout.EAST)
        return panel
    }

    private fun settingsPanel(): JPanel {
        val panel = JPanel()
        panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)
        panel.add(separator(PluginBundle.getMessage("settings.namespace.separator"), settings::nsSeparator))
        panel.add(separator(PluginBundle.getMessage("settings.key.separator"), settings::keySeparator))
        panel.add(separator(PluginBundle.getMessage("settings.plural.separator"), settings::pluralSeparator))
        panel.add(formUtils.textArea(PluginBundle.getMessage("settings.default.namespace"), settings::defaultNs))
        return panel
    }
}

/**
 * Settings configuration panel
 */
class PlainObjectSettingsFormFragment(val settings: PoSettings): SettingsFormFragment<PoSettings> {

    private val formUtils = FormUtils()

    private val modificationCheck = ModificationCheck<PoSettings>()

    private val initialHash = modificationCheck.hash(settings)

    override fun isModified(): Boolean = initialHash == modificationCheck.hash(settings)

    /**
     * Returns Settings main panel
     */
    override fun getRootPanel(): JPanel {
        val root = JPanel()
        root.layout = BorderLayout()
        root.add(DefaultComponentFactory.getInstance().createSeparator("Plain Object Settings"), BorderLayout.NORTH)
        root.add(settingsPanel(), BorderLayout.WEST)
        return root
    }

    private fun settingsPanel(): JPanel {
        val panel = JPanel()
        panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)
        panel.add(formUtils.checkbox(PluginBundle.getMessage("settings.gettext.enabled"), settings::gettext))
        panel.add(formUtils.textInput(PluginBundle.getMessage("settings.gettext.aliases"), settings::gettextAliases))
        return panel
    }
}

/**
 * Settings configuration panel
 */
class VueSettingsFormFragment(val settings: VueSettings): SettingsFormFragment<VueSettings> {

    private val modificationCheck = ModificationCheck<VueSettings>()

    private val initialHash = modificationCheck.hash(settings)

    override fun isModified(): Boolean = initialHash == modificationCheck.hash(settings)

    /**
     * Returns Settings main panel
     */
    override fun getRootPanel(): JPanel {
        val root = JPanel()
        root.layout = BorderLayout()
        root.add(DefaultComponentFactory.getInstance().createSeparator("Vue-i18n Settings"), BorderLayout.NORTH)
        root.add(settingsPanel(), BorderLayout.WEST)
        return root
    }

    private fun textInput(label:String, property: KMutableProperty0<String>):JPanel {
        val panel = JPanel()
        panel.layout = BorderLayout()
        panel.preferredSize = Dimension(350, 30)
        panel.add(JLabel(label), BorderLayout.WEST)
        val control = JTextField(property.get())
        control.name = label
        addLimitationsAndHandlers(control, 100, property::set)
        control.preferredSize = Dimension(100, 30)
        panel.add(control, BorderLayout.EAST)
        return panel
    }

    private fun vue(): JPanel {
        val panel = JPanel()
        panel.layout = BorderLayout()
        panel.preferredSize = Dimension(350, 30)
        val label = PluginBundle.getMessage("settings.vue")
        val vueMode = JCheckBox(label, settings.vue)
        vueMode.name = label
        vueMode.addItemListener { _ ->
            settings.vue = vueMode.isSelected
        }
        panel.add(vueMode, BorderLayout.WEST)
        return panel
    }

    private fun settingsPanel(): JPanel {
        val panel = JPanel()
        panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)
        panel.add(vue())
        panel.add(textInput(PluginBundle.getMessage("settings.vue.locales.directory"), settings::vueDirectory))
        return panel
    }
}