plugins {
    kotlin("jvm") version "1.9.23"
    id("org.jetbrains.compose") version "1.6.2"
}

group = "com.vandenbreemen"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    google()

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

    val videoDisplayRasterVersion = "1.0.2.4"
    implementation("com.vandenbreemen:video-display-raster:$videoDisplayRasterVersion")

    implementation(compose.runtime)
    implementation(compose.ui)
    implementation(compose.foundation)
    implementation(compose.material)
    implementation(compose.desktop.currentOs)
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)

    compose.desktop {
        application {
            mainClass = "MainKt"
        }
    }
}