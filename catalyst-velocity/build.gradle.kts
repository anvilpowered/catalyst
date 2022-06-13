val anvilVersion: String by project
val collectionsVersion: String by project
val apacheLoggingVersion: String by project
val apacheAsyncVersion: String by project
val apacheClientVersion: String by project
val apacheCoreVersion: String by project
val apacheMimeVersion: String by project
val apacheNioVersion: String by project
val emojiVersion: String by project
val minimessageVersion: String by project
val jacksonVersion: String by project
val jdaVersion: String by project
val jsonVersion: String by project
val kyoriVersion: String by project
val websocketVersion: String by project
val okhttp3Version: String by project
val okioVersion: String by project
val unirestVersion: String by project
val troveVersion: String by project

plugins {
    kotlin("jvm")
    id("org.jetbrains.kotlin.kapt")
    id("com.github.johnrengelman.shadow") version "5.2.0"
}

repositories {
    maven("https://repo.velocitypowered.com/snapshots/")
    maven("https://libraries.minecraft.net/")
}

dependencies {
    implementation(project(":catalyst-common"))
    implementation("net.kyori:adventure-text-minimessage:$minimessageVersion")
    compileOnly("com.velocitypowered:velocity-api:3.1.0-SNAPSHOT")
    kapt("com.velocitypowered:velocity-api:3.1.0-SNAPSHOT")
}
