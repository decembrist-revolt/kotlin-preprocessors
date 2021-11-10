package org.decembrist.di

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies

class ContextFilesGenerator(
    private val codeGenerator: CodeGenerator,
    private val contextData: ContextData,
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
        ContextTemplateEngine(file).render(contextData)
    }

    companion object {
        const val CONTEXT_FILENAME = "Context"
    }
}