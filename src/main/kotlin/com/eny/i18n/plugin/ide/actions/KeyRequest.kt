package com.eny.i18n.plugin.ide.actions

import com.eny.i18n.plugin.ide.settings.Settings
import com.eny.i18n.plugin.key.FullKey
import com.eny.i18n.plugin.key.parser.KeyParserBuilder
import com.eny.i18n.plugin.parser.RawKey
import com.eny.i18n.plugin.utils.KeyElement
import com.eny.i18n.plugin.utils.PluginBundle
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
        val config = Settings.getInstance(project).config()
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
                (if(config.gettext) KeyParserBuilder.withoutTokenizer() else KeyParserBuilder.withSeparators(config.nsSeparator, config.keySeparator)).build()
                    .parse(
                        RawKey(listOf(KeyElement.literal(keyStr))),
                        emptyNamespace = config.gettext,
                        firstComponentNamespace = config.firstComponentNs
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