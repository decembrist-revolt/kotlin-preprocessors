package org.decembrist.di

class DependencyService(private val context: Context) {

    fun resolveDependencies() {
        context.getDependencies().forEach { dependency ->
            dependency.resolveInjected()
            val isResolved = dependency.injected.all { it.dependency.resolved }
            if (isResolved) {
                context.markResolved(dependency)
            }
        }
        val dependencies = context.getDependencies()
        if (dependencies.any { !it.resolved }) {
            checkResolved(dependencies)
        }
    }

    private fun checkResolved(dependencies: Collection<Dependency>) {
        val unresolved = dependencies.filter { !it.resolved }
        val unresolvedCountOld = unresolved.size
        unresolved.forEach { dependency ->
            val isResolved = dependency.injected.all { it.dependency.resolved }
            if (isResolved) {
                context.markResolved(dependency)
            }
        }
        val unresolvedCount = unresolved.count { !it.resolved }
        if (unresolvedCount >= unresolvedCountOld) {
            val unresolvedTypes = unresolved.map(Dependency::type)
            throw UnresolvableDependencies(unresolvedTypes)
        }
        if (unresolvedCount > 0) {
            checkResolved(unresolved)
        }
    }

    private fun Dependency.resolveInjected() {
        for (injected in injected) {
            injected.dependency = if (injected.name != null) {
                val dependency = context[injected.name]
                    ?: throw NamedDependencyNotFoundError(injected.name, injected.type, type)
                if (!dependency.isSubtypeOf(injected.type)) {
                    throw NamedDependencyWrongTypeError(injected.name, injected.type, type)
                }
                dependency
            } else {
                context[injected.type]
                    ?: findDependencyByType(injected.type)
                    ?: findDependencyBySuperType(injected.type)
                    ?: throw DependencyNotFoundError(injected.type, type)
            }
        }
    }

    private fun Dependency.findDependencyByType(searchType: String): Dependency? {
        val dependencies = context.getDependenciesByType(searchType)
        if (dependencies.size > 1) throw MultipleDependenciesFoundError(searchType, type)
        return dependencies.firstOrNull()?.also { dependency ->
            context[searchType] = dependency
        }
    }

    private fun Dependency.findDependencyBySuperType(searchType: String): Dependency? {
        val dependencies = context.getDependenciesBySuperType(searchType)
        if (dependencies.size > 1) throw MultipleDependenciesFoundError(searchType, type)
        return dependencies.firstOrNull()?.also { dependency ->
            context[searchType] = dependency
        }
    }

    private fun Dependency.isSubtypeOf(type: String): Boolean =
        this.type == type || type in context.getSuperTypesByType(this.type)

}