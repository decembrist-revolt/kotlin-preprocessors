plugins {
    kotlin("jvm")
    id("com.google.devtools.ksp")
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

    compileOnly("org.decembrist:preprocessor-dependency-injection:1.0.3")
}

kotlin {
    sourceSets.main {
        kotlin.srcDir("build/generated/ksp/main/kotlin")
    }
}

ksp {
    // to specify generated Context.kt file package (default: root package)
    arg("rootPackage", "org.decembrist")
}