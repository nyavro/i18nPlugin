package com.eny.i18n.plugin.ide.settings

import com.eny.i18n.Extensions
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity

class StartupAction : StartupActivity {
    override fun runActivity(project: Project) {
        Extensions.TECHNOLOGY.extensionList.forEach {
            it.initialize(project)
        }
    }
}
