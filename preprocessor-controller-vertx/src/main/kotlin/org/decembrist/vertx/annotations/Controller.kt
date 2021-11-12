package org.decembrist.vertx.annotations

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class Controller(val value: String = "")
