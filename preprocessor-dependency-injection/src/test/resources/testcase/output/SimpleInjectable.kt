import kotlin.reflect.KFunction

val diContext: Map<String, Any> by lazy {
    val data = mutableMapOf<String, Any>()
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
    val testcaseinputComposition1 = testcase.input.Composition1(testcaseinputSimpleInjectable1, simpleInjectable2, simpleInjectable3, testcaseinputSimpleInjectable4, testcaseinputSimpleInjectable5, testcase.input.injectableFunction(testcaseinputSimpleInjectable5))
    data["testcase.input.Composition1"] = testcaseinputComposition1
    val testcaseinputComposition2 = testcase.input.Composition2(testcaseinputComposition1)
    data["testcase.input.Composition2"] = testcaseinputComposition2
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
