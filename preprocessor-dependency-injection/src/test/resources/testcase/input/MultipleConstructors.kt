package testcase.input

import org.decembrist.di.annotations.Injectable

@Injectable
class MultipleConstructors() {
    constructor(some: String): this()
}