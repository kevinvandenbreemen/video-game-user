plugins {
    kotlin("jvm") version "1.9.23"
}

group = "com.vandenbreemen"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()

    //  Attempting to use the package, based on instructions at
    //  https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-gradle-registry#using-a-published-package
    repositories {
        maven {
            url = uri("https://maven.pkg.github.com/kevinvandenbreemen/video-display-raster")
            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getenv("USERNAME")
                password = project.findProperty("gpr.key") as String? ?: System.getenv("TOKEN")
            }
        }
    }
}

dependencies {
    testImplementation(kotlin("test"))

    val videoDisplayRasterVersion = "1.0.1"
    implementation("com.vandenbreemen:video-display-raster:$videoDisplayRasterVersion")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}