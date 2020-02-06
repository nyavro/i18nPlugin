package com.eny.i18n.plugin.utils

import com.eny.i18n.plugin.ide.settings.Settings
import com.intellij.openapi.project.Project
import com.intellij.psi.search.GlobalSearchScope

/**
 * Gets project's search scope
 */
fun Settings.searchScope(project: Project): GlobalSearchScope =
    if (this.searchInProjectOnly) GlobalSearchScope.projectScope(project)
    else GlobalSearchScope.allScope(project)