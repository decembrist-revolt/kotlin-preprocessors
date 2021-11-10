package testcase.input

import org.decembrist.di.annotations.Injectable

class Parent {
    @Injectable
    fun child() = ""
}