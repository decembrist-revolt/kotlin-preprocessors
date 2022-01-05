package org.decembrist

import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import com.tschuchort.compiletesting.kspIncremental
import com.tschuchort.compiletesting.kspSourcesDir
import com.tschuchort.compiletesting.symbolProcessorProviders
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import org.decembrist.controller.BaseProcessorProvider
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.io.File

class ControllerProcessorTest {

    @ParameterizedTest
    @ValueSource(strings = ["Controller1", "Controller2"])
    fun `should generate router success`(fileName: String) {
        val compilation = setUpCompilation(fileName)
        val result = compilation.compile()
        result.exitCode shouldBe KotlinCompilation.ExitCode.OK
        val ktFiles = compilation.kspSourcesDir.walkTopDown()
            .filter { it.extension == "kt" }
            .toList()
        ktFiles.size shouldBe 1
        ktFiles.first().readText().replace("\r", "") shouldBe
                File("$TESTCASE_FOLDER/$OUTPUT/${fileName}Router.kt").readText().replace("\r", "")
    }

    @Test
    fun `should generate router base path success`() {
        val compilation = setUpCompilation("BasePathController1")
        val result = compilation.compile()
        result.exitCode shouldBe KotlinCompilation.ExitCode.OK
        val ktFiles = compilation.kspSourcesDir.walkTopDown()
            .filter { it.extension == "kt" }
            .toList()
        ktFiles.size shouldBe 1
        ktFiles.first().readText().replace("\r", "") shouldBe
                File("$TESTCASE_FOLDER/$OUTPUT/BasePathController1Router.kt").readText().replace("\r", "")
    }

    @Test
    fun `should generate router without base path success`() {
        val compilation = setUpCompilation("BasePathController2")
        val result = compilation.compile()
        result.exitCode shouldBe KotlinCompilation.ExitCode.OK
        val ktFiles = compilation.kspSourcesDir.walkTopDown()
            .filter { it.extension == "kt" }
            .toList()
        ktFiles.size shouldBe 1
        ktFiles.first().readText().replace("\r", "") shouldBe
                File("$TESTCASE_FOLDER/$OUTPUT/BasePathController2Router.kt").readText().replace("\r", "")
    }

    @Test
    fun `should generate router with body handler success`() {
        val compilation = setUpCompilation("BodyHandlerController")
        val result = compilation.compile()
        result.exitCode shouldBe KotlinCompilation.ExitCode.OK
        val ktFiles = compilation.kspSourcesDir.walkTopDown()
            .filter { it.extension == "kt" }
            .toList()
        ktFiles.size shouldBe 1
        ktFiles.first().readText().replace("\r", "") shouldBe
                File("$TESTCASE_FOLDER/$OUTPUT/BodyHandlerControllerRouter.kt").readText().replace("\r", "")
    }

    @Test
    fun `should fail cause of empty path`() {
        val compilation = setUpCompilation("EmptyPathArgumentController")
        val result = compilation.compile()
        result.exitCode shouldBe KotlinCompilation.ExitCode.COMPILATION_ERROR
        result.messages shouldContain "Annotation @GET has empty path argument for method testcase.input.EmptyPathArgumentController.emptyPath()"
    }

    @Test
    fun `should fail cause of private`() {
        val compilation = setUpCompilation("PrivateController1")
        val result = compilation.compile()
        result.exitCode shouldBe KotlinCompilation.ExitCode.COMPILATION_ERROR
        result.messages shouldContain "You try to use @Controller on private class: PrivateController1"
    }

    @Test
    fun `should fail cause of inner`() {
        val compilation = setUpCompilation("InnerController1")
        val result = compilation.compile()
        result.exitCode shouldBe KotlinCompilation.ExitCode.COMPILATION_ERROR
        result.messages shouldContain "You try to use @Controller on inner class: Parent.InnerController1"
    }

    @Test
    fun `should fail cause of sub class`() {
        val compilation = setUpCompilation("StaticController1")
        val result = compilation.compile()
        result.exitCode shouldBe KotlinCompilation.ExitCode.COMPILATION_ERROR
        result.messages shouldContain "You try to use @Controller on non top level class: Parent.StaticController1"
    }

    @Test
    fun `should fail cause of method empty param`() {
        val compilation = setUpCompilation("EmptyMethodParamController")
        val result = compilation.compile()
        result.exitCode shouldBe KotlinCompilation.ExitCode.COMPILATION_ERROR
        result.messages shouldContain "Method testcase.input.EmptyMethodParamController.wrong() should have only one io.vertx.ext.web.RoutingContext parameter"
    }

    @Test
    fun `should fail cause of method wrong type param`() {
        val compilation = setUpCompilation("WrontMethodParamTypeController")
        val result = compilation.compile()
        result.exitCode shouldBe KotlinCompilation.ExitCode.COMPILATION_ERROR
        result.messages shouldContain "Method testcase.input.WrontMethodParamTypeController.wrong(wrong) should have only one io.vertx.ext.web.RoutingContext parameter"
    }

    @Test
    fun `should fail cause of method multiple params`() {
        val compilation = setUpCompilation("MultipleMethodParamController")
        val result = compilation.compile()
        result.exitCode shouldBe KotlinCompilation.ExitCode.COMPILATION_ERROR
        result.messages shouldContain "Method testcase.input.MultipleMethodParamController.wrong() should have only one io.vertx.ext.web.RoutingContext parameter"
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