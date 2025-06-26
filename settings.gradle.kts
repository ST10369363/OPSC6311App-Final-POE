pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io") // Needed for MPAndroidChart
    }
}


rootProject.name = "OPSC6311App"
include(":app")
 