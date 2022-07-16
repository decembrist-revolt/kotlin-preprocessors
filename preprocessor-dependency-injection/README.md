# Preprocessor dependency injection

Code generation tool for spring style dependency injection with **ZERO** new runtime dependencies for _Kotlin_  
Tool tested on **[Google KSP](https://github.com/google/ksp)** version 1.7.10-1.0.6

[Example projects](../examples/dependency-injection)

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
    kotlin("jvm") version "1.7.10"
    // google ksp
    id("com.google.devtools.ksp") version "1.7.10-1.0.6"
}
```

```kotlin
dependencies {
    // enable tool with ksp
    ksp("org.decembrist:preprocessor-dependency-injection:1.0.3")
    // annotations
    implementation("org.decembrist:preprocessor-dependency-injection:1.0.3")
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
    // prints "Hello world!"
    controller.method()
}
```

KSP will generate file `build/generated/ksp/main/kotlin/Context.kt` after compile  
`Context.kt`

```kotlin
package org.decembrist

val diContext: Map<String, Any> get() = if (contextBuilt) _diContext else emptyMap()
var contextBuilt = false

private lateinit var _diContext: Map<String, Any>

/**
 * @param name T class qualifiedName by default (if @Injectable name was not specified)
 * @return injectable by name
 */
@Suppress("UNCHECKED_CAST")
inline fun <reified T : Any> getInstance(
    name: String = T::class.qualifiedName!!,
    parentContext: Map<String, Any>? = null
): T {
    val instance = resolveInstance<T>(name, parentContext)
    return if (instance !is T) (instance as (() -> T))() else instance
}

fun <T> resolveInstance(name: String, parentContext: Map<String, Any>? = null): Any {
    if (!contextBuilt) {
        _diContext = buildContext(parentContext ?: emptyMap())
        contextBuilt = true
    }
    return diContext[name] ?: throw RuntimeException("@Injectable by name $name not found")
}

/**
 * Build dependencies map (could be used as parent context)
 */
fun buildContext(parentContext: Map<String, Any> = emptyMap()): Map<String, Any> {
    val data = parentContext.toMutableMap()
    val orgdecembristServiceImpl = org.decembrist.ServiceImpl()
    data["org.decembrist.ServiceImpl"] = orgdecembristServiceImpl
    val orgdecembristPseudoController = org.decembrist.PseudoController(orgdecembristServiceImpl)
    data["org.decembrist.PseudoController"] = orgdecembristPseudoController
    return data
}
```

You can use getInstance fun to get instance from context

```kotlin
fun main() {
    val controller = getInstance<PseudoController>()
    controller.method()
}
```

@Injectable function (invokes on every @Inject)

```kotlin
@Injectable
class InjectParamClass

@Injectable
fun service(val param: InjectParamClass) = Service(param)
```

@Inject by name

```kotlin
@Injectable("s1")
class Service

@Injectable
class Controller(@Inject("s1") val service: Service)

...
val service = getInstance<Service>("s1")
```

@External annotation marks parameter as external dependency (somewhere from parent context)  
Param parentContext is simple Map<String, Any> of dependency names and actual instances

```kotlin
@Injectable
class SomeController(val external: ExternalLibraryClass)

fun main() {
    val controller = getInstance<SomeController>(
        parentContext = mapOf(ExternalLibraryClass::class.qualifiedName!! to ExternalLibraryClass())
    )
}
```

Code generated func buildContext() could be used to export dependency context as library  
Every injected param is @External by default if was not found in context  
To prevent param to be @External you could use @External(false)

```kotlin
// external.jar
package external.library

@Injectable
class LibraryInjectable

// your code
@Injectable
class MyClass(@External val external: LibraryInjectable)

fun main() {
    val myClass = getInstance<LibraryInjectable>(parentContext = external.library.buildContext())
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

* Generic dependencies.  
  (
  _Now_

```kotlin
class Service(val injected: Service1<*>)
```

only supported over

```kotlin
class Service(val injected: Service1<sometype>)
```

)

* Customizable dependency priority

Tested kotlin version 1.7.10