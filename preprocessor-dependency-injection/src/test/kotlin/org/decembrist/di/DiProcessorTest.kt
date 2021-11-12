package org.decembrist.di

import com.tschuchort.compiletesting.*
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import org.decembrist.di.DiProcessor.Companion.ROOT_PACKAGE
import org.junit.jupiter.api.Test
import java.io.File

class DiProcessorTest {

    @Test
    fun `simple inject success`() {
        val compilation = setUpCompilation("SimpleInjectable")
        val result = compilation.compile()
        result.exitCode shouldBe KotlinCompilation.ExitCode.OK
        val ktFiles = compilation.kspSourcesDir.walkTopDown()
            .filter { it.extension == "kt" }
            .toList()
        ktFiles.size shouldBe 1
        ktFiles.first().readText().replace("\r", "") shouldBe
                File("$TESTCASE_FOLDER/$OUTPUT/SimpleInjectable.kt").readText().replace("\r", "")
    }

    @Test
    fun `simple inject without package success`() {
        val compilation = setUpCompilation("SimpleInjectableWithoutPackage").apply {
            kspArgs = mutableMapOf(ROOT_PACKAGE to "testcase.output")
        }
        val result = compilation.compile()
        result.exitCode shouldBe KotlinCompilation.ExitCode.OK
        val ktFiles = compilation.kspSourcesDir.walkTopDown()
            .filter { it.extension == "kt" }
            .toList()
        ktFiles.size shouldBe 1
        ktFiles.first().readText().replace("\r", "") shouldBe
                File("$TESTCASE_FOLDER/$OUTPUT/SimpleInjectableWithoutPackage.kt").readText().replace("\r", "")
    }

    @Test
    fun `named dependency named not found error`() {
        val compilation = setUpCompilation("NamedDependencyNotFoundError").apply {
            kspArgs = mutableMapOf(ROOT_PACKAGE to "testcase.output")
        }
        val result = compilation.compile()
        result.exitCode shouldBe KotlinCompilation.ExitCode.COMPILATION_ERROR
        result.messages shouldContain "org.decembrist.di.NamedDependencyNotFoundError: Named @Injectable with name [notFound] and type testcase.input.IInjectable not found for class testcase.input.SimpleClass"
    }

    @Test
    fun `named dependency type not found error`() {
        val compilation = setUpCompilation("NamedDependencyWrongTypeError").apply {
            kspArgs = mutableMapOf(ROOT_PACKAGE to "testcase.output")
        }
        val result = compilation.compile()
        result.exitCode shouldBe KotlinCompilation.ExitCode.COMPILATION_ERROR
        result.messages shouldContain "org.decembrist.di.NamedDependencyWrongTypeError: Named @Injectable with name [name] should be testcase.input.IInjectable type for class testcase.input.SimpleClass"
    }

    @Test
    fun `multiple dependencies error`() {
        val compilation = setUpCompilation("MultipleDependenciesFoundError").apply {
            kspArgs = mutableMapOf(ROOT_PACKAGE to "testcase.output")
        }
        val result = compilation.compile()
        result.exitCode shouldBe KotlinCompilation.ExitCode.COMPILATION_ERROR
        result.messages shouldContain "org.decembrist.di.MultipleDependenciesFoundError: More than one @Injectable of type testcase.input.InjectableClass found for class testcase.input.SimpleClass"
    }

    @Test
    fun `multiple dependencies by supertype error`() {
        val compilation = setUpCompilation("MultipleDependenciesBySupertypeError").apply {
            kspArgs = mutableMapOf(ROOT_PACKAGE to "testcase.output")
        }
        val result = compilation.compile()
        result.exitCode shouldBe KotlinCompilation.ExitCode.COMPILATION_ERROR
        result.messages shouldContain "org.decembrist.di.MultipleDependenciesFoundError: More than one @Injectable of type testcase.input.IInjectable found for class testcase.input.SimpleClass"
    }

    @Test
    fun `dependency not found error`() {
        val compilation = setUpCompilation("DependencyNotFoundError").apply {
            kspArgs = mutableMapOf(ROOT_PACKAGE to "testcase.output")
        }
        val result = compilation.compile()
        result.exitCode shouldBe KotlinCompilation.ExitCode.COMPILATION_ERROR
        result.messages shouldContain "org.decembrist.di.DependencyNotFoundError: @Injectable of type testcase.input.IInjected not found for class testcase.input.NoInjected"
    }

    @Test
    fun `unresolvable dependency error`() {
        val compilation = setUpCompilation("UnresolvableDependencies")
        val result = compilation.compile()
        result.exitCode shouldBe KotlinCompilation.ExitCode.COMPILATION_ERROR
        result.messages shouldContain "org.decembrist.di.UnresolvableDependencies: One or more injected params for types [testcase.input.UnresolvableDependencies1, testcase.input.UnresolvableDependencies2] couldn't be resolved"
    }

    @Test
    fun `any injectable type error`() {
        val compilation = setUpCompilation("AnyInjectable")
        val result = compilation.compile()
        result.exitCode shouldBe KotlinCompilation.ExitCode.COMPILATION_ERROR
        result.messages shouldContain "org.decembrist.di.WrongInjectableTypeError: You try to use type [kotlin.Any] as @Injectable. It's not supported"
    }

    @Test
    fun `Object injectable type error`() {
        val compilation = setUpCompilation("ObjectInjectable")
        val result = compilation.compile()
        result.exitCode shouldBe KotlinCompilation.ExitCode.COMPILATION_ERROR
        result.messages shouldContain "org.decembrist.di.WrongInjectableTypeError: You try to use type [java.lang.Object] as @Injectable. It's not supported"
    }

    @Test
    fun `private class injectable error`() {
        val compilation = setUpCompilation("PrivateClass")
        val result = compilation.compile()
        result.exitCode shouldBe KotlinCompilation.ExitCode.COMPILATION_ERROR
        result.messages shouldContain "org.decembrist.di.WrongTargetModifierError: @Injectable not working for PRIVATE target: testcase.input.PrivateClass"
    }

    @Test
    fun `private fun injectable error`() {
        val compilation = setUpCompilation("PrivateFun")
        val result = compilation.compile()
        result.exitCode shouldBe KotlinCompilation.ExitCode.COMPILATION_ERROR
        result.messages shouldContain "org.decembrist.di.WrongTargetModifierError: @Injectable not working for PRIVATE target: testcase.input.privateFun"
    }

    @Test
    fun `abstract class injectable error`() {
        val compilation = setUpCompilation("AbstractClass")
        val result = compilation.compile()
        result.exitCode shouldBe KotlinCompilation.ExitCode.COMPILATION_ERROR
        result.messages shouldContain "org.decembrist.di.WrongTargetModifierError: @Injectable not working for ABSTRACT target: testcase.input.AbstractClass"
    }

    @Test
    fun `suspend fun injectable error`() {
        val compilation = setUpCompilation("SuspendFun")
        val result = compilation.compile()
        result.exitCode shouldBe KotlinCompilation.ExitCode.COMPILATION_ERROR
        result.messages shouldContain "org.decembrist.di.WrongTargetModifierError: @Injectable not working for SUSPEND target: testcase.input.privateFun"
    }

    @Test
    fun `non top level class error`() {
        val compilation = setUpCompilation("NonTopLevelClass")
        val result = compilation.compile()
        result.exitCode shouldBe KotlinCompilation.ExitCode.COMPILATION_ERROR
        result.messages shouldContain "org.decembrist.di.NonTopLevelInjectable: You try to use @Injectable on non top level entity: testcase.input.Parent.Child"
    }

    @Test
    fun `non top level fun error`() {
        val compilation = setUpCompilation("NonTopLevelFun")
        val result = compilation.compile()
        result.exitCode shouldBe KotlinCompilation.ExitCode.COMPILATION_ERROR
        result.messages shouldContain "org.decembrist.di.NonTopLevelInjectable: You try to use @Injectable on non top level entity: testcase.input.Parent.child"
    }

    @Test
    fun `interface injectable error`() {
        val compilation = setUpCompilation("InterfaceInjectable")
        val result = compilation.compile()
        result.exitCode shouldBe KotlinCompilation.ExitCode.COMPILATION_ERROR
        result.messages shouldContain "org.decembrist.di.WrongInjectableTargetError: You try to use @Injectable on wrong entity type [interface]: testcase.input.IInjectable"
    }

    @Test
    fun `private constructor error`() {
        val compilation = setUpCompilation("PrivateConstructor")
        val result = compilation.compile()
        result.exitCode shouldBe KotlinCompilation.ExitCode.COMPILATION_ERROR
        result.messages shouldContain "org.decembrist.di.AmbiguousConstructorError: @Injectable class should have only one non private constructor: testcase.input.PrivateConstructor"
    }

    @Test
    fun `multiple constructors error`() {
        val compilation = setUpCompilation("MultipleConstructors")
        val result = compilation.compile()
        result.exitCode shouldBe KotlinCompilation.ExitCode.COMPILATION_ERROR
        result.messages shouldContain "org.decembrist.di.AmbiguousConstructorError: @Injectable class should have only one non private constructor: testcase.input.MultipleConstructors"
    }

    @Test
    fun `generic injected error`() {
        val compilation = setUpCompilation("GenericInjectable")
        val result = compilation.compile()
        result.exitCode shouldBe KotlinCompilation.ExitCode.COMPILATION_ERROR
        result.messages shouldContain "org.decembrist.di.GenericInjectedError: Non STAR generic types as @Injectable param not supported yet, use <*> instead: testcase.input.Service<INVARIANT Some2>"
    }

    private fun setUpCompilation(fileName: String) = KotlinCompilation().apply {
        sources = listOf(
            SourceFile.new(
                "$fileName.kt",
                File("$TESTCASE_FOLDER/$INPUT/$fileName.kt").readText()
            )
        )
        symbolProcessorProviders = listOf(BaseProcessorProvider())
        inheritClassPath = true
        kspIncremental = false
    }

    companion object {
        const val TESTCASE_FOLDER = "src/test/resources/testcase"
        const val INPUT = "input"
        const val OUTPUT = "output"
    }

}