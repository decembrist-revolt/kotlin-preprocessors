package testcase.input

import org.decembrist.di.annotations.External
import org.decembrist.di.annotations.Inject
import org.decembrist.di.annotations.Injectable

interface IInjectable

// TODO check private constructor
@Injectable
class UnNamed: IInjectable

@Injectable
class SimpleClass(@External(false) @Inject("notFound") injected: IInjectable)