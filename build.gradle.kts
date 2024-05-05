plugins {
    kotlin("jvm") version "1.9.23"
}

group = "com.vandenbreemen"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    repositories {
        maven {
            url = uri("https://maven.pkg.github.com/OWNER/REPOSITORY")
            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getenv("USERNAME")
                password = project.findProperty("gpr.key") as String? ?: System.getenv("TOKEN")
            }
        }
    }
}

dependencies {
    testImplementation(kotlin("test"))

    implementation("com.vandenbreemen:video-display-raster:1.0.0")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}