plugins {
    kotlin("jvm") version "1.6.10"
    id("com.google.devtools.ksp") version "1.6.10-1.0.2"
}

group = "org.decembrist"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    ksp("org.decembrist:preprocessor-controller-vertx:1.0.3")

    implementation("io.vertx:vertx-web:4.2.2")
    compileOnly("org.decembrist:preprocessor-controller-vertx:1.0.3")
}

kotlin {
    sourceSets.main {
        kotlin.srcDir("build/generated/ksp/main/kotlin")
    }
}