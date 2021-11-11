package org.decembrist

import getInstance
import org.decembrist.di.annotations.Inject
import org.decembrist.di.annotations.Injectable

fun main() {
    val controller = getInstance<PseudoController>()
    // prints 'property1'
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
    fun method() = println(config.property)
}

@Injectable
class PseudoService(@Inject("dependencyName")
                    private val repository: PseudoRepository) : Service {
    override fun method() = repository.method()
}

@Injectable
class PseudoController(private val service: Service) {
    fun method() = service.method()
}