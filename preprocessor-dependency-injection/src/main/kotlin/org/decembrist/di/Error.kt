package org.decembrist.di

import com.google.devtools.ksp.symbol.Modifier

class NamedDependencyNotFoundError(name: String, type: String, targetType: String) :
    RuntimeException("Named @Injectable with name [$name] and type $type not found for class $targetType")

class NamedDependencyWrongTypeError(name: String, type: String, targetType: String) :
    RuntimeException("Named @Injectable with name [$name] should be $type type for class $targetType")

class MultipleDependenciesFoundError(type: String, targetType: String) :
    RuntimeException("More than one @Injectable of type $type found for class $targetType")

class DependencyNotFoundError(type: String, targetType: String) :
    RuntimeException("@Injectable of type $type not found for class $targetType")

class UnresolvableDependencies(types: List<String>) :
    RuntimeException("One or more injected params for types [${types.joinToString()}] couldn't be resolved")

class WrongInjectableTypeError(type: String) :
    RuntimeException("You try to use type [$type] as @Injectable. It's not supported")

class WrongTargetModifierError(modifier: Modifier, target: String) :
    RuntimeException("@Injectable not working for $modifier target: $target")

class NonTopLevelInjectable(type: String) :
    RuntimeException("You try to use @Injectable on non top level entity: $type")

class WrongDependencyNameError(wrongName: String) :
    RuntimeException("Dependency name $wrongName should start from letter and contain only [a-zA-Z0-9]")

class WrongInjectableTargetError(classKind: String, type: String) :
    RuntimeException("You try to use @Injectable on wrong entity type [$classKind]: $type")

class AmbiguousConstructorError(type: String) :
    RuntimeException("@Injectable class should have only one non private constructor: $type")

class GenericInjectedError(type: String) :
    RuntimeException("Non STAR generic types as @Injectable param not supported yet, use <*> instead: $type")