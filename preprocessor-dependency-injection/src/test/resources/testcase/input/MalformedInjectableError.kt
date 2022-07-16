package testcase.input

import org.decembrist.di.annotations.Injectable

class Generic<T>

typealias Typealias = Generic<String>

@Injectable
class MalformedInjectableError(val myString: Typealias)