package testcase.input

import org.decembrist.di.annotations.Inject
import org.decembrist.di.annotations.Injectable

interface IInjectable

@Injectable("name")
class Named

@Injectable
class SimpleClass(@Inject("name") injected: IInjectable)