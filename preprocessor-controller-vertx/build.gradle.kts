val kotlinVersion: String by project
val vertxVersion: String by project
val kotestVersion: String by project
val jupiterVersion: String by project
val kotlinCompileTestingVersion: String by project
val kspVersion: String by project

val isReleaseVersion = !(version as String).endsWith("SNAPSHOT")

plugins {
    kotlin("jvm")
    id("signing")
    id("maven-publish")
    id("java-library")
    id("org.jetbrains.dokka") version "1.5.30"
}

group = "org.decembrist"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("io.insert-koin:koin-core:3.1.2")
    implementation("com.google.devtools.ksp:symbol-processing-api:$kspVersion")

    testImplementation("com.github.tschuchortdev:kotlin-compile-testing-ksp:$kotlinCompileTestingVersion")
    testImplementation("io.vertx:vertx-web:$vertxVersion")
    testImplementation("org.junit.jupiter:junit-jupiter:$jupiterVersion")
    testImplementation("io.kotest:kotest-assertions-core-jvm:$kotestVersion")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

//tasks.withType<Sign> {
//    onlyIf { isReleaseVersion }
//}

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

publishing {
    publications {
        create<MavenPublication>("shadow") {
            artifactId = "preprocessor-controller-vertx"
            version = "1.0.0"
            description = "Code generation tool for vertx spring style controllers"
            artifact(sourcesJar)
            artifact(dokkaJavadocJar)
            from(components["java"])
            pom {
                name.set("Decembrist preprocessor controller vertx")
                description.set("Code generation tool for vertx spring style controllers")
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