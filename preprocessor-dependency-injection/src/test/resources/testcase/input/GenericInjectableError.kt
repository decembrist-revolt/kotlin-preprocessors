package testcase.input

import org.decembrist.di.annotations.Injectable

@Injectable
class Service<T>

typealias ServiceString = Service<String>

@Injectable
class GenericInjectable(val some: ServiceString)