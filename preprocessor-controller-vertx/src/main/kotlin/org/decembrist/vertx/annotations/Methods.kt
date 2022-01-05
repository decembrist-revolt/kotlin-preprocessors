package org.decembrist.vertx.annotations

@Repeatable
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class REQUEST(
    val method: Array<String>,
    val path: String = "/",
    val bodyHandler: BodyHandler = BodyHandler()
)

@Repeatable
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class OPTIONS(val path: String = "/", val bodyHandler: BodyHandler = BodyHandler())

@Repeatable
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class GET(val path: String = "/", val bodyHandler: BodyHandler = BodyHandler())

@Repeatable
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class HEAD(val path: String = "/", val bodyHandler: BodyHandler = BodyHandler())

@Repeatable
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class POST(val path: String = "/", val bodyHandler: BodyHandler = BodyHandler())

@Repeatable
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class PUT(val path: String = "/", val bodyHandler: BodyHandler = BodyHandler())

@Repeatable
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class DELETE(val path: String = "/", val bodyHandler: BodyHandler = BodyHandler())

@Repeatable
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class TRACE(val path: String = "/", val bodyHandler: BodyHandler = BodyHandler())

@Repeatable
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class CONNECT(val path: String = "/", val bodyHandler: BodyHandler = BodyHandler())

@Repeatable
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class PATCH(val path: String = "/", val bodyHandler: BodyHandler = BodyHandler())

@Repeatable
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class PROPFIND(val path: String = "/", val bodyHandler: BodyHandler = BodyHandler())

@Repeatable
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class PROPPATCH(val path: String = "/", val bodyHandler: BodyHandler = BodyHandler())

@Repeatable
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class MKCOL(val path: String = "/", val bodyHandler: BodyHandler = BodyHandler())

@Repeatable
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class COPY(val path: String = "/", val bodyHandler: BodyHandler = BodyHandler())

@Repeatable
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class MOVE(val path: String = "/", val bodyHandler: BodyHandler = BodyHandler())

@Repeatable
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class LOCK(val path: String = "/", val bodyHandler: BodyHandler = BodyHandler())

@Repeatable
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class UNLOCK(val path: String = "/", val bodyHandler: BodyHandler = BodyHandler())

@Repeatable
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class MKCALENDAR(val path: String = "/", val bodyHandler: BodyHandler = BodyHandler())

@Repeatable
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class VERSION_CONTROL(val path: String = "/", val bodyHandler: BodyHandler = BodyHandler())

@Repeatable
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class REPORT(val path: String = "/", val bodyHandler: BodyHandler = BodyHandler())

@Repeatable
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class CHECKOUT(val path: String = "/", val bodyHandler: BodyHandler = BodyHandler())

@Repeatable
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class CHECKIN(val path: String = "/", val bodyHandler: BodyHandler = BodyHandler())

@Repeatable
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class UNCHECKOUT(val path: String = "/", val bodyHandler: BodyHandler = BodyHandler())

@Repeatable
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class MKWORKSPACE(val path: String = "/", val bodyHandler: BodyHandler = BodyHandler())

@Repeatable
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class UPDATE(val path: String = "/", val bodyHandler: BodyHandler = BodyHandler())

@Repeatable
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class LABEL(val path: String = "/", val bodyHandler: BodyHandler = BodyHandler())

@Repeatable
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class MERGE(val path: String = "/", val bodyHandler: BodyHandler = BodyHandler())

@Repeatable
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class BASELINE_CONTROL(val path: String = "/", val bodyHandler: BodyHandler = BodyHandler())

@Repeatable
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class MKACTIVITY(val path: String = "/", val bodyHandler: BodyHandler = BodyHandler())

@Repeatable
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class ORDERPATCH(val path: String = "/", val bodyHandler: BodyHandler = BodyHandler())

@Repeatable
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class ACL(val path: String = "/", val bodyHandler: BodyHandler = BodyHandler())

@Repeatable
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class SEARCH(val path: String = "/", val bodyHandler: BodyHandler = BodyHandler())

@Repeatable
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class ALL(val path: String = "/", val bodyHandler: BodyHandler = BodyHandler())