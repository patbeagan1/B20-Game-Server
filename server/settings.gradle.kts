@file:Suppress("UnstableApiUsage")

rootProject.name = "ktor-demo"

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

include("config")
include("hello")

include(":console-vision")
include(":base")
include(":domain")