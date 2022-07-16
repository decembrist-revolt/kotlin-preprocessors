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
    val testcaseinputStringList = testcase.input.StringList()
    data["testcase.input.StringList"] = testcaseinputStringList
    val testcaseinputSimpleInjectable1 = testcase.input.SimpleInjectable1()
    data["testcase.input.SimpleInjectable1"] = testcaseinputSimpleInjectable1
    val simpleInjectable2 = testcase.input.SimpleInjectable2()
    data["simpleInjectable2"] = simpleInjectable2
    val simpleInjectable3 = testcase.input.SimpleInjectable3()
    data["simpleInjectable3"] = simpleInjectable3
    val testcaseinputSimpleInjectable4 = testcase.input.SimpleInjectable4()
    data["testcase.input.SimpleInjectable4"] = testcaseinputSimpleInjectable4
    val testcaseinputSimpleInjectable5 = testcase.input.SimpleInjectable5()
    data["testcase.input.SimpleInjectable5"] = testcaseinputSimpleInjectable5
    data["testcase.input.SimpleInjectable6"] = { testcase.input.injectableFunction(testcaseinputSimpleInjectable5) }
    val testcaseinputComposition1 = testcase.input.Composition1(
        testcaseinputSimpleInjectable1,
        simpleInjectable2,
        simpleInjectable3,
        testcaseinputSimpleInjectable4,
        testcaseinputSimpleInjectable5,
        testcase.input.injectableFunction(testcaseinputSimpleInjectable5),
        getOrInvoke<testcase.input.SimpleInjectable7>(
            "testcase.input.SimpleInjectable7",
            data["testcase.input.SimpleInjectable7"]
        ),
        getOrInvoke<testcase.input.SimpleInjectable7>(
            "testcase.input.SimpleInjectable7",
            data["testcase.input.SimpleInjectable7"]
        ),
        getOrInvoke<java.util.ArrayList>("java.util.ArrayList", data["java.util.ArrayList"])
    )
    data["testcase.input.Composition1"] = testcaseinputComposition1
    val testcaseinputComposition2 = testcase.input.Composition2(testcaseinputComposition1)
    data["testcase.input.Composition2"] = testcaseinputComposition2
    return data
}

