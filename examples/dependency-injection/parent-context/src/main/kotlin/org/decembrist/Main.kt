package org.decembrist

import org.decembrist.di.annotations.External
import org.decembrist.di.annotations.Inject
import org.decembrist.di.annotations.Injectable
import org.decembrist.external.ExternalService


fun main() {
    // it's possible to pass parent context, useful for external libraries
    val controller = getInstance<PseudoController>(parentContext = org.decembrist.external.buildContext())
    // prints 'property1 ExternalService
    //         ExternalService
    //         external'
    controller.method()
}

interface Service {
    fun method()
}

interface Config {
    val property: String
}

class PseudoConfig(override val property: String) : Config

@Injectable
fun config(): Config = PseudoConfig("property1")

@Injectable("dependencyName")
class PseudoRepository(private val config: Config) {
    fun method(message: String) = println("${config.property} $message")
}

@Injectable
class PseudoService(
    @Inject("dependencyName")
    private val repository: PseudoRepository,
    // @External annotation marks parameter as external dependency (somewhere from parent context)
    @External
    private val externalService1: ExternalService,
    // @External annotation could be omitted
    private val externalService2: ExternalService,
    @Inject("external-string")
    private val externalString: String
) : Service {
    override fun method() =
        repository.method(
            """
            ${externalService1.externalServiceMessage} 
            ${externalService2.externalServiceMessage}
            $externalString
            """.trimIndent()
        )
}

@Injectable
class PseudoController(private val service: Service) {
    fun method() = service.method()
}