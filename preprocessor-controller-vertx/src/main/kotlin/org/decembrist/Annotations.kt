package org.decembrist

import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSAnnotation
import kotlin.reflect.KClass

fun KSAnnotated.getAnnotationsOfType(annotationClass: KClass<out Annotation>): Sequence<KSAnnotation> =
    annotations.filter {
        it.shortName.getShortName() == annotationClass.simpleName &&
                it.annotationType.resolve().declaration.qualifiedName?.asString() == annotationClass.qualifiedName
    }

@Suppress("UNCHECKED_CAST")
fun <T> KSAnnotation.getArgumentValue(name: String) = arguments.first { it.name!!.asString() == name }.value as T