package com.eny.i18n.plugin.ide.refactoring

import com.intellij.json.psi.JsonElementGenerator
import com.intellij.openapi.util.TextRange
import com.intellij.psi.AbstractElementManipulator

class PropertyKeyManipulator : AbstractElementManipulator<CurrentPsi>() {

    override fun handleContentChange(element: CurrentPsi, range: TextRange, newContent: String): CurrentPsi {
        val newEl = JsonElementGenerator(element.project).createProperty(newContent, element.element.text)
        return CurrentPsi(element.element.parent.replace(newEl))
    }
}