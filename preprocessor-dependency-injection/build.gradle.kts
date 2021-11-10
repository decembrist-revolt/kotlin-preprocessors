val kotestVersion: String by project
val jupiterVersion: String by project
val kotlinCompileTestingVersion: String by project
val kspVersion: String by project

plugins {
    kotlin("jvm")
}

repositories {
    mavenCentral()
}

group = "org.decembrist"
version = "1.0.0"

dependencies {
    implementation(kotlin("stdlib"))
    implementation("com.google.devtools.ksp:symbol-processing-api:$kspVersion")
    implementation(project(":preprocessors-core"))

    testImplementation("com.github.tschuchortdev:kotlin-compile-testing-ksp:$kotlinCompileTestingVersion")
    testImplementation("org.junit.jupiter:junit-jupiter:$jupiterVersion")
    testImplementation("io.kotest:kotest-assertions-core-jvm:$kotestVersion")
}

tasks.withType<Test> {
    useJUnitPlatform()
}