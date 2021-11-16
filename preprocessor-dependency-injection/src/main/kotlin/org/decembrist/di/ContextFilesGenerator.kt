package org.decembrist.di

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger

class ContextFilesGenerator(
    private val codeGenerator: CodeGenerator,
    private val contextData: ContextData,
    private val logger: KSPLogger,
) {

    fun generate() {
        generateRoot()
    }

    private fun generateRoot() {
        val rootPackage = contextData.options.rootPackage
        val files = contextData.dependencies.map(Dependency::file).distinct().toTypedArray()
        val file = codeGenerator.createNewFile(
            Dependencies(true, *files),
            rootPackage,
            CONTEXT_FILENAME
        )
        ContextTemplateEngine(file, logger).render(contextData)
    }

    companion object {
        const val CONTEXT_FILENAME = "Context"
    }
}