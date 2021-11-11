package org.decembrist.di.annotations

@Target(AnnotationTarget.TYPE_PARAMETER, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.SOURCE)
annotation class Inject(val name: String)

@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class Injectable(val name: String = "")