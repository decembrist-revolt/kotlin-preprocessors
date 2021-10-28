val kotlinVersion: String by project
val vertxVersion: String by project
val jupiterVersion: String by project
val kotestVersion: String by project

plugins {
    kotlin("jvm") version "1.5.31"
    id("com.google.devtools.ksp") version "1.5.31-1.0.0"
    id("io.github.gradle-nexus.publish-plugin") version "1.1.0"
}

group = "org.decembrist"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    ksp(project(":preprocessor-controller-vertx"))

    testImplementation("io.vertx:vertx-web:$vertxVersion")
    testImplementation("io.vertx:vertx-lang-kotlin-coroutines:$vertxVersion")
    testImplementation("io.vertx:vertx-junit5:$vertxVersion")
    testImplementation(project(":preprocessor-controller-vertx"))
    testImplementation("org.junit.jupiter:junit-jupiter:$jupiterVersion")
    testImplementation("io.kotest:kotest-assertions-core-jvm:$kotestVersion")
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        events = setOf(
            org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED,
            org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED,
            org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED
        )
    }
}

kotlin {
    sourceSets.test {
        kotlin.srcDir("build/generated/ksp/test/kotlin")
    }
}

nexusPublishing {
    repositories {
        sonatype {
            username.set(System.getenv("SONATYPE_USERNAME"))
            password.set(System.getenv("SONATYPE_PASSWORD"))
        }
    }
    useStaging.set(true)
    repositoryDescription.set("Code generation tools for everything")
}

subprojects {
    tasks.withType<JavaCompile>().configureEach {
        sourceCompatibility = JavaVersion.VERSION_1_8.toString()
        targetCompatibility = JavaVersion.VERSION_1_8.toString()
    }
}