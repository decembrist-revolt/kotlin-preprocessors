package testcase.input

import org.decembrist.di.annotations.Injectable

interface IInjected

@Injectable
class NoInjected(injeced: IInjected)