package org.decembrist.controller

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.Modifier.INNER
import com.google.devtools.ksp.symbol.Modifier.PRIVATE
import org.decembrist.preprocessors.utils.*

fun KSClassDeclaration.checkModifiers() {
    val className = qualifiedName!!.asString()
    when(findFirstModifierOrNull(INNER, PRIVATE)) {
        INNER -> error("You try to use @Controller on inner class: $className")
        PRIVATE -> error("You try to use @Controller on private class: $className")
    }
    if (!isTopLevel()) {
        error("You try to use @Controller on non top level class: $className")
    }
}

fun KSFunctionDeclaration.checkRouteMethodParameters() {
    if (parameters.size != 1) {
        error(
            "Method ${getFullName()}() " +
                    "should have only one io.vertx.ext.web.RoutingContext parameter"
        )
    }
    val parameter = parameters.first()
    if (parameter.type.getClassName() != "io.vertx.ext.web.RoutingContext") {
        val paramName = parameter.name!!.asString()
        error(
            "Method ${getFullName()}($paramName) " +
                    "should have only one io.vertx.ext.web.RoutingContext parameter"
        )
    }
}