package org.decembrist

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import org.decembrist.controller.ControllerProcessor
import org.decembrist.controller.ControllerVisitor
import org.decembrist.controller.RouteVisitor
import org.koin.core.context.startKoin
import org.koin.dsl.module

class BaseProcessorProvider: SymbolProcessorProvider {

    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        val koin = startKoin {
            val module = module {
                single { environment.codeGenerator }
                single { environment.logger }
                single { TemplateEngine() }
                single<SymbolProcessor> { ControllerProcessor() }
                single { ControllerVisitor() }
                single { RouteVisitor() }
            }
            modules(module)
        }.koin
        return koin.get()
    }

}