package org.decembrist.controller

import com.google.devtools.ksp.getDeclaredFunctions
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.validate
import org.decembrist.RouterTemplateEngine
import org.decembrist.preprocessors.utils.getAnnotationOfType
import org.decembrist.preprocessors.utils.getArgumentValue
import org.decembrist.preprocessors.utils.getFullClassName
import org.decembrist.preprocessors.utils.getPackageName
import org.decembrist.vertx.annotations.Controller

class ControllerProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        resolver.getSymbolsWithAnnotation(Controller::class.qualifiedName!!).forEach {
            if (it is KSClassDeclaration && it.validate()) {
                it.processController()
            }
        }

        return emptyList()
    }

    private fun KSClassDeclaration.processController() {
        val annotation = getAnnotationOfType<Controller>()
        checkModifiers()
        val packageName = getPackageName()
        val className = getFullClassName()
        val parentPath = annotation.getArgumentValue<String>(Controller::value.name)
        val routes = getRoutes()
        val data = RouterData(
            parentPath = parentPath,
            packageName = packageName,
            controllerClass = className,
            routes = routes,
            bodyHandler = routes.any { it.bodyHandler.enabled }
        )
        val fileName = className + "Router"
        val file = codeGenerator.createNewFile(
            Dependencies(true, containingFile!!),
            packageName,
            fileName
        )
        RouterTemplateEngine(file).render(data)
        logger.info("$fileName was created", this)
    }

    private fun KSClassDeclaration.getRoutes(): List<RouteData> = getDeclaredFunctions().flatMap { function ->
        if (function.validate()) {
            function.getRouteData()
        } else emptyList()
    }.toList()

}