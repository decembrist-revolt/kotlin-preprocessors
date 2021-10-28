package org.decembrist.controller

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSNode
import com.google.devtools.ksp.symbol.Modifier.INNER
import com.google.devtools.ksp.symbol.Modifier.PRIVATE
import com.google.devtools.ksp.visitor.KSEmptyVisitor
import org.koin.core.component.KoinComponent

class ControllerVisitor : KSEmptyVisitor<RouterData, Unit>(), KoinComponent {

    override fun defaultHandler(node: KSNode, data: RouterData) {
    }

    override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: RouterData) {
        classDeclaration.checkModifiers()
        data.apply {
            packageName = classDeclaration.packageName.asString()
            controllerClass = classDeclaration.qualifiedName!!.asString()
        }
    }

    private fun KSClassDeclaration.checkModifiers() {
        val className = qualifiedName!!.asString()
        when {
            INNER in modifiers -> error("You try to use @Controller on inner class: $className")
            PRIVATE in modifiers -> error("You try to use @Controller on private class: $className")
            parent?.let { it is KSClassDeclaration } == true ->
                error("You try to use @Controller on sub class: $className")
        }
    }

}