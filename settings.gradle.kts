@file:Suppress("UnstableApiUsage")

dependencyResolutionManagement {
    repositories {
        mavenLocal()
        mavenCentral()
        maven("https://libraries.minecraft.net")
        maven("https://repo.papermc.io/repository/maven-public/")
    }
}

pluginManagement {
    includeBuild("build-logic")
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

rootProject.name = "catalyst"

sequenceOf(
    "agent",
    "agent-application",
    "agent-mc-velocity",
    "core",
    "core-domain",
).forEach {
    val project = ":catalyst-$it"
    include(project)
    project(project).projectDir = file(it.replace('-', '/'))
}
