pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "TP_Taller_de_Programacion"
include(":androidApp")
include(":shared")