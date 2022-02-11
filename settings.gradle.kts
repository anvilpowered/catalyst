rootProject.name = "Catalyst"

include(":catalyst-api")
include(":catalyst-common")
include(":catalyst-velocity")

pluginManagement {
    plugins {
        val kotlinVersion: String by settings
        kotlin("jvm") version kotlinVersion
        id("net.kyori.blossom") version "1.3.0"
    }
}
