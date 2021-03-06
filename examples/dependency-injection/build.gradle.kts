plugins {
    kotlin("jvm") version "1.7.10"
    id("com.google.devtools.ksp") version "1.7.10-1.0.6"
}

group = "org.decembrist"
version = "1.0.0"

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    ksp("org.decembrist:preprocessor-dependency-injection:1.0.3")
    implementation(project(":external-library"))

    compileOnly("org.decembrist:preprocessor-dependency-injection:1.0.2")
}

kotlin {
    sourceSets.main {
        kotlin.srcDir("build/generated/ksp/main/kotlin")
    }
}