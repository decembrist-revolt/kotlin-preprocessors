package org.decembrist.preprocessors.utils

import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSAnnotation
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1

inline fun <reified T : Annotation> KSAnnotated.getAnnotationsOfType(): Sequence<KSAnnotation> =
    getAnnotationsOfType(T::class)

fun KSAnnotated.getAnnotationsOfType(annotationClass: KClass<out Annotation>): Sequence<KSAnnotation> =
    annotations.filter {
        it.shortName.getShortName() == annotationClass.simpleName &&
                it.annotationType.getClassName() == annotationClass.qualifiedName
    }

inline fun <reified T : Annotation> KSAnnotated.getAnnotationOfType(): KSAnnotation = getAnnotationOfType(T::class)

fun KSAnnotated.getAnnotationOfType(annotationClass: KClass<out Annotation>): KSAnnotation =
    getAnnotationOfTypeOrNull(annotationClass) ?: throw NoSuchElementException("Sequence is empty.")

inline fun <reified T : Annotation> KSAnnotated.getAnnotationOfTypeOrNull(): KSAnnotation? =
    getAnnotationOfTypeOrNull(T::class)

fun KSAnnotated.getAnnotationOfTypeOrNull(annotationClass: KClass<out Annotation>): KSAnnotation? =
    annotations.firstOrNull {
        it.shortName.getShortName() == annotationClass.simpleName &&
                it.annotationType.resolve().declaration.qualifiedName?.asString() == annotationClass.qualifiedName
    }

fun <A : Annotation, T> KSAnnotation.getArgumentValue(property: KProperty1<A, T>) =
    getArgumentValue<T>(property.name)

@Suppress("UNCHECKED_CAST")
fun <T> KSAnnotation.getArgumentValue(name: String) = arguments.first { it.name!!.asString() == name }.value as T

fun KSAnnotation.getAnnotationArgumentAsMap(name: String): Map<String, Any?> =
    arguments.first { it.name!!.asString() == name }
        .run { value as KSAnnotation }
        .run { arguments.associate { it.name!!.getShortName() to it.value } }