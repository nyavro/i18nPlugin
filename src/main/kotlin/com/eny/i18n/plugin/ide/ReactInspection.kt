package com.eny.i18n.plugin.ide

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.lang.javascript.psi.*
import com.intellij.lang.javascript.psi.ecma6.TypeScriptPropertySignature
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

            private fun returnsFunction(call: JSCallExpression): Boolean {
                val res = PsiTreeUtil.findChildOfType(call, JSReferenceExpression::class.java)?.resolve() ?: return false
                val jsFunction = PsiTreeUtil.findChildOfType(res, JSFunctionExpression::class.java)
                if (jsFunction?.block != null) return false
                return PsiTreeUtil.findChildOfType(jsFunction, JSFunctionExpression::class.java) != null
            }

            override fun visitJSEmbeddedContent(embeddedContent: JSEmbeddedContent?) {
                if (embeddedContent != null && isEmbeddedInXMLAttribute(embeddedContent)) {
                    if (isReference(embeddedContent)) {
                        val resolved = PsiTreeUtil.findChildOfType(embeddedContent, JSReferenceExpression::class.java)?.resolve()
                        val item = PsiTreeUtil.findCommonParent(resolved, embeddedContent)
                        if (item != null && item is JSBlockStatement) {
                            holder.registerProblem(embeddedContent, "Handler in render")
                        }
                    } else {
                        if (PsiTreeUtil.findChildOfType(embeddedContent, JSFunction::class.java) != null) {
                            holder.registerProblem(embeddedContent, "Handler in render 2")
                        } else {
                            val call = PsiTreeUtil.findChildOfType(embeddedContent, JSCallExpression::class.java)
                            if (call!=null && returnsFunction(call)) {
                                holder.registerProblem(embeddedContent, "Handler in render 3")
                            }
                        }
                    }
                }
            }

            override fun visitJSDestructuringElement(destructuringElement: JSDestructuringElement?) {
                if(destructuringElement != null) {
                    val item = PsiTreeUtil.findChildOfType(destructuringElement, JSReferenceExpression::class.java)?.resolve()
                    if (item != null && item is TypeScriptPropertySignature && item.isOptional) {
                        holder.registerProblem(destructuringElement, "Destruct")
                    }
                }
            }

            private fun isReference(node: JSEmbeddedContent): Boolean =
                node.children.size == 1 && node.children[0] is JSReferenceExpression

            private fun isEmbeddedInXMLAttribute(node: JSEmbeddedContent): Boolean =
                node.parent?.parent is XmlAttributeValue

            private fun isProps(name: String?) =
                (name != null) && name.split(".").contains("props")

            override fun visitJSReferenceExpression(node: JSReferenceExpression?) {
                visitJSExpression(node)
            }

            override fun visitJSAssignmentExpression(node: JSAssignmentExpression?) {
                if (node != null) {
                    if (isProps(node.lOperand?.text)) {
                        holder.registerProblem(node, "Assignment 1")
                    } else {
                        val source = PsiTreeUtil.findChildOfType(
                                PsiTreeUtil.findChildOfType(node?.lOperand, JSReferenceExpression::class.java),
                                JSReferenceExpression::class.java
                        )?.resolve()
                        val parent = PsiTreeUtil.getParentOfType(source, JSDestructuringElement::class.java)
                        if (isProps(parent?.initializer?.text)) {
                            holder.registerProblem(node, "Assignment 2")
                        }
                    }
                }
            }
        }
    }
}
