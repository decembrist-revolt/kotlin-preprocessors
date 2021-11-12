package org.decembrist.preprocessors.generator

import java.io.OutputStream

abstract class AbstractTemplateEngine<T>(private val file: OutputStream) {

    var isRendered: Boolean = false
        private set

    fun render(data: T) {
        if (isRendered) throw RuntimeException("Already rendered")
        renderBody(data)
        file.close()
        isRendered = true
    }

    protected abstract fun renderBody(data: T)

    protected fun writePackageLine(packageName: String?) {
        val packageLine = packageName.takeIf { it.isNullOrBlank().not() }?.let { "package $it" }
        if (packageLine != null) {
            write { packageLine }
            nextLine(2)
        }
    }

    protected fun nextLine(repeat: Int = 1) = file.write(System.lineSeparator().repeat(repeat).toByteArray())

    protected fun write(contentBlock: () -> String) = file.write(contentBlock().toByteArray())

    protected fun writeLine(contentBlock: () -> String) {
        write(contentBlock)
        nextLine()
    }
}