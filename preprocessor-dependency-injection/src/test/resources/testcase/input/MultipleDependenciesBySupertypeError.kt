package testcase.input

import org.decembrist.di.annotations.Inject
import org.decembrist.di.annotations.Injectable

interface IInjectable

@Injectable
class Injectable1: IInjectable

@Injectable
class Injectable2: IInjectable

@Injectable
class SimpleClass(injected: IInjectable)