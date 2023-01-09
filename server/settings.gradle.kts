@file:Suppress("UnstableApiUsage")

rootProject.name = "b20-demo"

dependencyResolutionManagement {
    val properties = java.util.Properties().apply {
        load(File(rootDir.absolutePath + "/local.properties").inputStream())
    }

    repositories {
        maven {
            name = "ConsoleVision"
            url = uri("https://maven.pkg.github.com/patbeagan1/ConsoleVision")
            credentials {
                username = properties.getProperty("github_username", null)
                password = properties.getProperty("github_password", null)
            }
        }
        google()
        mavenCentral()
    }
}

include(":base")
include(":domain")
include(":app")