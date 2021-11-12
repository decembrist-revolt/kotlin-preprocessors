plugins {
    kotlin("jvm") version "1.5.31"
    id("com.google.devtools.ksp") version "1.5.31-1.0.0"
}

group = "org.decembrist"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    ksp("org.decembrist:preprocessor-controller-vertx:1.0.1")

    implementation("io.vertx:vertx-web:4.1.5")
    implementation("org.decembrist:preprocessor-controller-vertx:1.0.1")
}

kotlin {
    sourceSets.main {
        kotlin.srcDir("build/generated/ksp/main/kotlin")
    }
}