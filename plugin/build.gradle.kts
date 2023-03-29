plugins {
    id("java-gradle-plugin")
    id("maven-publish")
    id("org.jetbrains.kotlin.jvm") version "1.8.10"
}

group = "com.glaze.something"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.docker.java)
    implementation(libs.docker.java.client)
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
}

publishing {
    publications {
        create<MavenPublication>("plugin") {
            groupId = "com.glaze"
            artifactId = "docker-plugin"
            version = "0.0.1"

            pom {
                name.set("Glaze docker plugin")
                description.set("A simple plugin for allowing docker image builds from gradle")
                url.set("https://github.com/Glazzes/gradle-docker-plugin")

                developers {
                    developer {
                        id.set("Glaze")
                        name.set("Santiago")
                        email.set("santiagop1@protonmail.com")
                    }
                }
            }
        }
    }
}

gradlePlugin {
    plugins {
        create("simplePlugin") {
            id = "com.github.glaze.docker-plugin"
            version = "0.0.1"
            implementationClass = "com.github.glaze.DockerPlugin"
        }
    }
}

val functionalTestSourceSet = sourceSets.create("functionalTest") {
}

configurations["functionalTestImplementation"].extendsFrom(configurations["testImplementation"])

val functionalTest by tasks.registering(Test::class) {
    testClassesDirs = functionalTestSourceSet.output.classesDirs
    classpath = functionalTestSourceSet.runtimeClasspath
    useJUnitPlatform()
}

gradlePlugin.testSourceSets(functionalTestSourceSet)

tasks.named<Task>("check") {
    dependsOn(functionalTest)
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
