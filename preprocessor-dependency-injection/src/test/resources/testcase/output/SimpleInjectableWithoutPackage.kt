package testcase.output

import kotlin.reflect.KFunction

val diContext: Map<String, Any> by lazy {
    val data = mutableMapOf<String, Any>()
    val SimpleInjectable1 = SimpleInjectable1()
    data["SimpleInjectable1"] = SimpleInjectable1
    val simpleInjectable2 = SimpleInjectable2()
    data["simpleInjectable2"] = simpleInjectable2
    val simpleInjectable3 = SimpleInjectable3()
    data["simpleInjectable3"] = simpleInjectable3
    val SimpleInjectable4 = SimpleInjectable4()
    data["SimpleInjectable4"] = SimpleInjectable4
    val SimpleInjectable5 = SimpleInjectable5()
    data["SimpleInjectable5"] = SimpleInjectable5
    data["SimpleInjectable6"] = { injectableFunction(SimpleInjectable5) }
    val Composition1 = Composition1(SimpleInjectable1, simpleInjectable2, simpleInjectable3, SimpleInjectable4, SimpleInjectable5, injectableFunction(SimpleInjectable5))
    data["Composition1"] = Composition1
    val Composition2 = Composition2(Composition1)
    data["Composition2"] = Composition2
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
