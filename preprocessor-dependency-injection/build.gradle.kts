val kotestVersion: String by project
val jupiterVersion: String by project
val kotlinCompileTestingVersion: String by project
val kspVersion: String by project

plugins {
    id("java")
    kotlin("jvm")
    id("signing")
    id("maven-publish")
    id("java-library")
    id("org.jetbrains.dokka") version "1.5.30"
    id("com.github.johnrengelman.shadow") version "7.1.0"
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

val sourcesJar by tasks.creating(Jar::class) {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    archiveClassifier.set("sources")
    from(sourceSets.main.map { it.allSource })
}

val dokkaJavadocJar by tasks.register<Jar>("dokkaJavadocJar") {
    dependsOn(tasks.dokkaJavadoc)
    from(tasks.dokkaJavadoc.flatMap { it.outputDirectory })
    archiveClassifier.set("javadoc")
}

signing {
    useGpgCmd()
    sign(extensions.getByType<PublishingExtension>().publications)
}

tasks.withType(com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar::class) {
    dependencies {
        include(project(":preprocessors-core"))
    }
    archiveClassifier.set("")
}

publishing {
    publications {
        create<MavenPublication>("shadow") {
            artifactId = "preprocessor-dependency-injection"
            version = "1.0.2"
            description = "Code generation tool for dependency injection"
            artifact(sourcesJar)
            artifact(dokkaJavadocJar)
            artifact(tasks["shadowJar"])
            pom {
                name.set("Decembrist preprocessor dependency injection")
                description.set("Code generation tool for dependency injection")
                url.set("https://github.com/decembrist-revolt/kotlin-preprocessors")

                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }

                developers {
                    developer {
                        id.set("decembrist")
                        name.set("Nikita Pometun")
                        email.set("epsilon840@gmail.com")
                    }
                }
                scm {
                    url.set("https://github.com/decembrist-revolt/kotlin-preprocessors")
                }
            }
        }
    }
}