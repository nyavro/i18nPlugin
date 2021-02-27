package com.eny.i18n.plugin.ide.actions

import com.eny.i18n.plugin.key.FullKey
import com.eny.i18n.plugin.key.parser.KeyParserBuilder
import com.eny.i18n.plugin.ide.PluginBundle
import com.eny.i18n.plugin.addons.technology.vue.vueSettings
import com.eny.i18n.plugin.ide.settings.i18NextSettings
import com.eny.i18n.plugin.key.KeyElement
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.InputValidator
import com.intellij.openapi.ui.Messages

/**
 * Key request result
 */
class KeyRequestResult(val key: FullKey?, val isCancelled: Boolean)

/**
 * Requests i18n key from user
 */
class KeyRequest {

    /**
     * Requests key
     */
    fun key(project: Project, text: String): KeyRequestResult {
        val config = project.i18NextSettings()
        val gettext = false//project.poSettings().gettext
        val keyStr = Messages.showInputDialog(
            project,
            String.format(PluginBundle.getMessage("action.intention.extract.key.hint"), text),
            PluginBundle.getMessage("action.intention.extract.key.input.key"),
            Messages.getQuestionIcon(),
            null,
            isValidKey())
        return if(keyStr == null) {
            KeyRequestResult(null, true)
        } else {
            KeyRequestResult(
                (if(gettext) KeyParserBuilder.withoutTokenizer() else KeyParserBuilder.withSeparators(config.nsSeparator, config.keySeparator)).build()
                    .parse(
                        Pair(listOf(KeyElement.literal(keyStr)), null),
                        emptyNamespace = project.vueSettings().vue || gettext
                    ),
                false
            )
        }
    }

    private fun isValidKey() = object : InputValidator {
        override fun checkInput(inputString: String?): Boolean {
            return (inputString ?: "").isNotEmpty()
        }
        override fun canClose(inputString: String?): Boolean = true
    }
}