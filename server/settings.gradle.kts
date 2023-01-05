rootProject.name = "ktor-demo"

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

include("config")
include("hello")

include(":console-vision")
include(":base")
include(":domain")