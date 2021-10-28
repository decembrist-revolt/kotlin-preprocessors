package org.decembrist.controller

import com.google.devtools.ksp.getDeclaredFunctions
import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.validate
import org.decembrist.TemplateEngine
import org.decembrist.getAnnotationsOfType
import org.decembrist.getArgumentValue
import org.decembrist.vertx.annotations.Controller
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ControllerProcessor : SymbolProcessor, KoinComponent {

    private val templateEngine: TemplateEngine by inject()
    private val codeGenerator: CodeGenerator by inject()
    private val logger: KSPLogger by inject()
    private val controllerVisitor: ControllerVisitor by inject()
    private val routeVisitor: RouteVisitor by inject()

    override fun process(resolver: Resolver): List<KSAnnotated> {
        resolver.getSymbolsWithAnnotation(Controller::class.qualifiedName!!).forEach {
            if (it is KSClassDeclaration && it.validate()) {
                it.processController()
            }
        }

        return emptyList()
    }

    private fun KSClassDeclaration.processController() {
        val annotation = getAnnotationsOfType(Controller::class).first()
        val data = RouterData(
            parentPath = annotation.getArgumentValue<String>(Controller::value.name)
        )
        accept(controllerVisitor, data)
        processRoutes(data)
        val (packageName, className) = data
        val fileName = className + "Router"
        val file = codeGenerator.createNewFile(
            Dependencies(true, containingFile!!),
            packageName.orEmpty(),
            fileName
        )
        templateEngine.renderRouter(file, data)
        file.close()
        logger.info("$fileName was created", this)
    }

    private fun KSClassDeclaration.processRoutes(data: RouterData) = getDeclaredFunctions().forEach {
        if (it.validate()) {
            it.accept(routeVisitor, data)
        }
    }

}