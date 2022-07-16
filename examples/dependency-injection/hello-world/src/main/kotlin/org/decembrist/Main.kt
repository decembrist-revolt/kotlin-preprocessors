package org.decembrist

import org.decembrist.di.annotations.Injectable

interface Service {
    fun method()
}

@Injectable
class ServiceImpl : Service {
    override fun method() = println("Hello world!")
}

@Injectable
class PseudoController(val service: Service) {
    fun method() = service.method()
}


fun main() {
    val controller = getInstance<PseudoController>()
    controller.method()
}