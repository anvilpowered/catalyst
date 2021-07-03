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
  java
  id("com.github.johnrengelman.shadow") version "5.2.0"
  id("org.jetbrains.kotlin.kapt")
}

repositories {
  maven("https://repo.velocitypowered.com/snapshots/")
  maven("https://libraries.minecraft.net/")
}

dependencies {
  implementation(project(":catalyst-api"))
  implementation(project(":catalyst-common"))

  implementation("com.velocitypowered:velocity-api:3.0.0-SNAPSHOT")
  kapt("com.velocitypowered:velocity-api:3.0.0-SNAPSHOT")
  implementation("net.kyori:adventure-text-minimessage:$minimessageVersion")
}

tasks {
  shadowJar {
    val jarName = "Catalyst-Velocity-${project.version}.jar"
    println("Building: $jarName")
    archiveFileName.set(jarName)

    dependencies {
      include(project(":catalyst-api"))
      include(project(":catalyst-common"))
      include(dependency("org.apache.commons:commons-collections4:$collectionsVersion"))
      include(dependency("commons-logging:commons-logging:$apacheLoggingVersion"))
      include(dependency("org.apache.httpcomponents:httpasyncclient:$apacheAsyncVersion"))
      include(dependency("org.apache.httpcomponents:httpclient:$apacheClientVersion"))
      include(dependency("org.apache.httpcomponents:httpcore:$apacheCoreVersion"))
      include(dependency("org.apache.httpcomponents:httpmime:$apacheMimeVersion"))
      include(dependency("org.apache.httpcomponents:httpcore-nio:$apacheNioVersion"))
      include(dependency("com.vdurmont:emoji-java:$emojiVersion"))
      include(dependency("net.kyori:adventure-text-minimessage:$minimessageVersion"))
      include(dependency("com.fasterxml.jackson.core:jackson-annotations:$jacksonVersion"))
      include(dependency("com.fasterxml.jackson.core:jackson-core:$jacksonVersion"))
      include(dependency("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion"))
      include(dependency("net.dv8tion:JDA:$jdaVersion"))
      include(dependency("org.json:json:$jsonVersion"))
      include(dependency("net.kyori:event-method-asm:$kyoriVersion"))
      include(dependency("com.neovisionaries:nv-websocket-client:$websocketVersion"))
      include(dependency("com.squareup.okhttp3:okhttp:$okhttp3Version"))
      include(dependency("net.kyori:event-method-asm:$kyoriVersion"))
      include(dependency("net.sf.trove4j:trove4j:$troveVersion"))
      include(dependency("com.mashape.unirest:unirest-java:$unirestVersion"))
    }
  }
}

artifacts {
  archives(tasks.shadowJar)
}
