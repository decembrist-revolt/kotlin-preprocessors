package testcase.input

import org.decembrist.di.annotations.Injectable

class InjectableClass

@Injectable("injectable1")
fun injectable1() = InjectableClass()

@Injectable("injectable2")
fun injectable2() = InjectableClass()

@Injectable
class SimpleClass(injected: InjectableClass)