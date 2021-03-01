package com.eny.i18n.plugin.addons.technology.vue

import com.eny.i18n.plugin.ide.LocalizationSourcesProvider
import com.eny.i18n.plugin.ide.LocalizationSource
import com.eny.i18n.plugin.ide.LocalizationType
import com.eny.i18n.plugin.utils.fromNullable
import com.eny.i18n.plugin.utils.maybe
import com.intellij.json.*
import com.intellij.json.psi.JsonObject
import com.intellij.lang.impl.PsiBuilderFactoryImpl
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.html.HtmlTag
import com.intellij.psi.impl.source.resolve.FileContextUtil
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.xml.XmlText

class VueSfcLocalizationSources : LocalizationSourcesProvider {

    override fun find(fileNames: List<String>, element: PsiElement?, isHost: Boolean, project: Project): List<LocalizationSource> {
        return PsiTreeUtil.findChildrenOfType(if (isHost) element?.containingFile else element?.containingFile?.getUserData(FileContextUtil.INJECTED_IN_ELEMENT)?.containingFile, HtmlTag::class.java)
            .toList()
            .find { it.name == "i18n" }
            ?.let { sfcSrcTag ->
                PsiTreeUtil.findChildOfType(sfcSrcTag, XmlText::class.java)?.let { sfcSourceText ->
                    val fileName = sfcSourceText.containingFile.name
                    JsonParser()
                        .parse(
                            JsonElementTypes.OBJECT,
                            PsiBuilderFactoryImpl().createBuilder(JsonParserDefinition(), JsonLexer(), sfcSourceText.text)
                        ).psi?.maybe<PsiElement, JsonObject>()
                        ?.propertyList
                        ?.map {
                            LocalizationSource(
                                    it.value!!,
                                    it.name,
                                    fileName,
                                    "SFC: ${fileName}/${it.name} ",
                                    LocalizationType(listOf(JsonFileType.INSTANCE), "vue-sfc"),
                                    sfcSourceText
                            )
                        }
                }
            }.fromNullable()
    }
}