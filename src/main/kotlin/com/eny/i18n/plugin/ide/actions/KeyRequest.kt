package com.eny.i18n.plugin.ide.actions

import com.eny.i18n.plugin.ide.settings.Settings
import com.eny.i18n.plugin.parser.ExpressionKeyParser
import com.eny.i18n.plugin.utils.FullKey
import com.eny.i18n.plugin.utils.KeyElement
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

    private val parser = ExpressionKeyParser()

    /**
     * Requests key
     */
    fun key(project: Project): KeyRequestResult {
        val settings = Settings.getInstance(project)
        val keyStr = Messages
            .showInputDialog(project, "Specify the key", "Input i18n key", Messages.getQuestionIcon(), null, isValidKey())
        return if(keyStr == null) {
            KeyRequestResult(null, true)
        } else {
            KeyRequestResult(
                parser.parse(listOf(KeyElement.literal(keyStr)),
                    nsSeparator = settings.nsSeparator,
                    keySeparator = settings.keySeparator,
                    stopCharacters = settings.stopCharacters,
                    emptyNamespace = settings.vue
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