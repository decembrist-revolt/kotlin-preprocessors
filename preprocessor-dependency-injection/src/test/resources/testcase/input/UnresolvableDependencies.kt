package testcase.input

import org.decembrist.di.annotations.Injectable

@Injectable
class UnresolvableDependencies1(unresolvableDependencies2: UnresolvableDependencies2)
@Injectable
class UnresolvableDependencies2(unresolvableDependencies1: UnresolvableDependencies1)