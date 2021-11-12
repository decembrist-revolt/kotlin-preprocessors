val kotlinVersion: String by project
val vertxVersion: String by project
val kotestVersion: String by project
val jupiterVersion: String by project
val kotlinCompileTestingVersion: String by project
val kspVersion: String by project

plugins {
    kotlin("jvm")
}

group = "org.decembrist"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("com.google.devtools.ksp:symbol-processing-api:$kspVersion")
    implementation(project(":preprocessor-controller-vertx"))
    implementation(project(":preprocessor-dependency-injection"))
}