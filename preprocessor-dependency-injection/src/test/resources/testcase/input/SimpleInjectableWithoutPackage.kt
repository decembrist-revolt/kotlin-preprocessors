import org.decembrist.di.annotations.Inject
import org.decembrist.di.annotations.Injectable

interface InjectableBase1
interface InjectableBase2
interface InjectableBase3<T>

@Injectable
class Composition2(
    val composition1: Composition1
)

@Injectable
class Composition1(
    val injectable1: InjectableBase1,
    @Inject("simpleInjectable2")
    val injectable2: InjectableBase2,
    @Inject("simpleInjectable3")
    val injectable3: InjectableBase2,
    val injectable4: SimpleInjectable4,
    val injectable5: InjectableBase3<*>,
    val injectable6: SimpleInjectable6,
)

@Injectable
open class SimpleInjectable1 : InjectableBase1

@Injectable("simpleInjectable2")
class SimpleInjectable2 : InjectableBase2

@Injectable("simpleInjectable3")
class SimpleInjectable3 : InjectableBase2

@Injectable
class SimpleInjectable4

abstract class SimpleInjectableParent: InjectableBase3<SimpleInjectableParent>

@Injectable
class SimpleInjectable5: SimpleInjectableParent()

class SimpleInjectable6(injectable5: SimpleInjectable5)

@Injectable
fun injectableFunction(injectable5: SimpleInjectable5): SimpleInjectable6 = SimpleInjectable6(injectable5)