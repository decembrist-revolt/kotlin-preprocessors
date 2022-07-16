package testcase.input

import org.decembrist.di.annotations.External
import org.decembrist.di.annotations.Injectable

interface IInjected

@Injectable
class NoInjected(@External(false) injeced: IInjected)