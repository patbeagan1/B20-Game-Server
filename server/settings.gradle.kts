@file:Suppress("UnstableApiUsage")

rootProject.name = "b20-demo"

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

include(":console-vision")
include(":base")
include(":domain")
include(":app")