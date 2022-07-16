package testcase.output

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
    return getOrInvoke(name, instance)
}

fun <T> resolveInstance(name: String, parentContext: Map<String, Any>? = null): Any? {
    if (!contextBuilt) {
        _diContext = buildContext(parentContext ?: emptyMap())
        contextBuilt = true
    }
    return diContext[name]
}

inline fun <reified T : Any> getOrInvoke(name: String, instance: Any?): T {
    instance ?: throw RuntimeException("@Injectable by name $name not found")
    return if (instance !is T) (instance as (() -> T))() else instance
}

/**
 * Build dependencies map (could be used as parent context)
 */
fun buildContext(parentContext: Map<String, Any> = emptyMap()): Map<String, Any> {
    val data = parentContext.toMutableMap()
    val StringList = StringList()
    data["StringList"] = StringList
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
    val Composition1 = Composition1(
        SimpleInjectable1,
        simpleInjectable2,
        simpleInjectable3,
        SimpleInjectable4,
        SimpleInjectable5,
        injectableFunction(SimpleInjectable5),
        getOrInvoke<SimpleInjectable7>("SimpleInjectable7", data["SimpleInjectable7"]),
        getOrInvoke<SimpleInjectable7>("SimpleInjectable7", data["SimpleInjectable7"]),
        getOrInvoke<java.util.ArrayList>("java.util.ArrayList", data["java.util.ArrayList"])
    )
    data["Composition1"] = Composition1
    val Composition2 = Composition2(Composition1)
    data["Composition2"] = Composition2
    return data
}

