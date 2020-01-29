package com.eny.i18n.plugin.ide

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.lang.javascript.psi.*
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.xml.XmlAttributeValue

class ReactInspection : LocalInspectionTool() {

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : JSElementVisitor() {
            private fun isCalledFromContextByName(function: JSFunction, name: String): Boolean {
                if (function.name == "renderHeader") {
                    println("here")
                }
                return false
            }

            private fun isInContextByName(node: PsiElement, name: String): Boolean {
                if (node is JSFunction) {
                    val flag = isCalledFromContextByName(node, name)
                }
                if (node is JSFunction && node.name != null) {
                    if (node.name == name) {
                        return true
                    }
                } else {
                    if (node.parent == null) {
                        return false
                    }
                }
                return isInContextByName(node.parent, name)
            }

            override fun visitJSFunctionExpression(node: JSFunctionExpression) {
                if (isInContextByName(node, "render")) {
//                    holder.registerProblem(node, "Function definition inside render")
                }
            }

            override fun visitJSCallExpression(node: JSCallExpression) {
                if (node.isValid && !node.isNewExpression && isInContextByName(node, "render")) {
                    println("here")
                }
                visitJSExpression(node)
            }

            override fun visitJSFunctionDeclaration(node: JSFunction) {
                super.visitJSFunctionDeclaration(node)
            }

            override fun visitJSEmbeddedContent(embeddedContent: JSEmbeddedContent?) {
                if (embeddedContent != null && isEmbeddedInXMLAttribute(embeddedContent) && isReference(embeddedContent)) {
                    val resolved = PsiTreeUtil.findChildOfType(embeddedContent, JSReferenceExpression::class.java)?.resolve()
                    val item = PsiTreeUtil.findCommonParent(resolved, embeddedContent)
                    if (item != null && item is JSBlockStatement) {
                        holder.registerProblem(embeddedContent, "Embedded content")
                    }
                }
            }

            private fun isReference(node: JSEmbeddedContent): Boolean =
                node.children.size == 1 && node.children[0] is JSReferenceExpression

            private fun isEmbeddedInXMLAttribute(node: JSEmbeddedContent): Boolean =
                node.parent?.parent is XmlAttributeValue

            override fun visitJSReferenceExpression(node: JSReferenceExpression?) {
                visitJSExpression(node)
            }
        }
    }
}
