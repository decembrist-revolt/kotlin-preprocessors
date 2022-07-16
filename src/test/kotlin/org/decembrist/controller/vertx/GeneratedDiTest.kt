package org.decembrist.controller.vertx

import org.decembrist.di.annotations.External
import org.decembrist.di.annotations.Inject
import org.decembrist.di.annotations.Injectable
import org.decembrist.getInstance
import org.junit.jupiter.api.Test
import java.util.concurrent.ThreadLocalRandom

class GeneratedDiTest {

    @Test
    fun `success path`() {
        val controller = getInstance<SomeController>(
            parentContext = mapOf(SomeService5::class.qualifiedName!! to SomeService5())
        )
        controller.someService1 === getInstance<SomeService1>()
        controller.someService2 === getInstance<SomeService2>()
        controller.someService1.someRepository === getInstance<SomeRepository>()
        controller.someService1.someRepository.config !== getInstance<SomeConfig>()
        controller.someService3 === getInstance<SomeService3>("service1")
        controller.someService4 === getInstance<SomeService4>("service2")
        getInstance<SomeConfig>() !== getInstance<SomeConfig>()
    }


}

@Injectable
class SomeController(
    val someService1: SomeService1,
    val someService2: SomeService2,
    @Inject("service1")
    val someService3: Service<*>,
    @Inject("service2")
    val someService4: Service<*>,
    @External
    val someService5: SomeService5,
)

@Injectable
class SomeService1(val someRepository: SomeRepository)

@Injectable
class SomeService2(val config: SomeConfig)

interface Service<T>

@Injectable("service1")
class SomeService3 : Service<SomeService3>

@Injectable("service2")
class SomeService4 : Service<SomeService4>

class SomeService5

@Injectable
class SomeRepository(val config: SomeConfig)

class SomeConfig(val random: Int = ThreadLocalRandom.current().nextInt())

@Injectable
fun someConfig() = SomeConfig()