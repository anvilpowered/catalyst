@file:Suppress("UnstableApiUsage")

dependencyResolutionManagement {
    repositories {
        maven("https://oss.sonatype.org/content/repositories/snapshots")
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
    "api",
    "core",
    "proxy",
).forEach {
    val project = ":catalyst-$it"
    include(project)
    project(project).projectDir = file(it.replace('-', '/'))
}
