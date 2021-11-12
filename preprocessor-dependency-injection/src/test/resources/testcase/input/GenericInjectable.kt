package testcase.input

import org.decembrist.di.annotations.Injectable

interface Service<T>

@Injectable
class Some1 : Service<Some1>

class Some2 : Service<Some2>

@Injectable
class GenericInjectable(val some: Service<Some2>)