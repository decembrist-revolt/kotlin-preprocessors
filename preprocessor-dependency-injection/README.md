# Preprocessor dependency injection

Code generation tool for spring style dependency injection with **ZERO** new runtime dependencies for _Kotlin_  
Tool uses **[Google KSP](https://github.com/google/ksp)** version 1.5.31-1.0.0

```kotlin
@Injectable
class Something

fun main() {
    val somethingSingleton = getInstance<Something>()
}
```

## Getting started:  
`build.gradle.kts`
```kotlin
plugins {  
    // or whatever kotlin you want
    kotlin("jvm") version "1.5.31"
    // google ksp
    id("com.google.devtools.ksp") version "1.5.31-1.0.0"
}
```

```kotlin
dependencies {
    // enable tool with ksp
    ksp("org.decembrist:preprocessor-dependency-injection:1.0.1")
    // annotations
    implementation("org.decembrist:preprocessor-dependency-injection:1.0.1")
    ...
    // add vertx web dependency to use
}
```

```kotlin
// include generated sources for compile
kotlin {
    sourceSets.main {
        kotlin.srcDir("build/generated/ksp/main/kotlin")
    }
}
```
`Main.kt`
```kotlin
import org.decembrist.di.annotations.Inject
import org.decembrist.di.annotations.Injectable

interface Service {
    fun method()
}

interface Config {
    val property: String
}

// make class injectable
// declare new singleton dependency with one injected parameter of type Server
@Injectable
class PseudoController(private val service: Service) {
    fun method() = service.method()
}

class PseudoConfig(override val property: String) : Config

// dependency name 
// for example: to avoid duplicate dependencies with same type
@Injectable("dependencyName")
class PseudoRepository(private val config: Config) {
    fun method() = println(config.property)
}

// functional dependency
// function is invoked for every place that needs this type dependency
@Injectable
fun config(): Config = PseudoConfig("property1")

@Injectable
class PseudoService(
    // to specify what exactly to inject 
    @Inject("dependencyName")
    private val repository: PseudoRepository) : Service {
    
    override fun method() = repository.method()
}

```
KSP will generate file `build/generated/ksp/main/kotlin/Context.kt` on compile  
`Context.kt`
```kotlin
import kotlin.reflect.KFunction

val diContext: Map<String, Any> by lazy {
    val data = mutableMapOf<String, Any>()
    data["org.decembrist.Config"] = { org.decembrist.config() }
    val dependencyName = org.decembrist.PseudoRepository(org.decembrist.config())
    data["dependencyName"] = dependencyName
    val orgdecembristPseudoService = org.decembrist.PseudoService(dependencyName)
    data["org.decembrist.PseudoService"] = orgdecembristPseudoService
    val orgdecembristPseudoController = org.decembrist.PseudoController(orgdecembristPseudoService)
    data["org.decembrist.PseudoController"] = orgdecembristPseudoController
    data
}

/**
 * @param name T class qualifiedName by default (if @Injectable name was not specified)
 * @return injectable by name
 */
inline fun <reified T: Any> getInstance(name: String = T::class.qualifiedName!!): T {
    val instance = diContext[name] ?: throw RuntimeException("@Injectable by name $name not found")
    return if (instance !is T) {
        (instance as (() -> T))()
    } else instance
}

```
You can use getInstance fun to get instance from context
```kotlin
fun main() {
    val controller = getInstance<PseudoController>()
    // print 'property1'
    controller.method()
}
```
KSP args
```kotlin
ksp {
    // to specify generated Context.kt file package (default: root package)
    arg("rootPackage", "org.decembrist")
}
```
### Road map
* Generic dependencies. _Now | class Service(val injected: Service1<*>) | only supported over | class Service(val injected: Service1< sometype >) |_
* Customizable dependency priority

Tested kotlin version 1.5.31