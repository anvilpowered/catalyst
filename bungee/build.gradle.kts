val anvilVersion: String by project
val collectionsVersion: String by project
val apacheLoggingVersion: String by project
val apacheAsyncVersion: String by project
val apacheClientVersion: String by project
val apacheCoreVersion: String by project
val apacheMimeVersion: String by project
val apacheNioVersion: String by project
val bungeeVersion: String by project
val configurateVersion: String by project
val emojiVersion: String by project
val minimessageVersion: String by project
val jacksonVersion: String by project
val jdaVersion: String by project
val jsonVersion: String by project
val kyoriVersion: String by project
val spigotVersion: String by project
val websocketVersion: String by project
val okhttp3Version: String by project
val okioVersion: String by project
val unirestVersion: String by project
val troveVersion: String by project

plugins {
  java
  id("com.github.johnrengelman.shadow") version "5.2.0"
}

repositories {
  mavenCentral()
  maven("https://jitpack.io")
  maven("https://oss.sonatype.org/content/repositories/snapshots")
  maven("https://repo.spongepowered.org/maven")
  maven("https://jetbrains.bintray.com/xodus")
  maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots")
}

dependencies {
  implementation(project(":catalyst-api"))
  implementation(project(":catalyst-common"))

  implementation("org.anvilpowered:anvil-api:$anvilVersion")
  implementation("org.apache.httpcomponents:httpasyncclient:$apacheAsyncVersion")
  implementation("org.apache.httpcomponents:httpclient:$apacheClientVersion")
  implementation("org.apache.httpcomponents:httpcore:$apacheCoreVersion")
  implementation("org.apache.httpcomponents:httpmime:$apacheMimeVersion")
  implementation("org.apache.httpcomponents:httpcore-nio:$apacheNioVersion")
  implementation("net.md-5:bungeecord-api:$bungeeVersion")
  implementation("net.dv8tion:JDA:$jdaVersion")
  implementation("org.spongepowered:configurate-core:$configurateVersion")
  implementation("org.spongepowered:configurate-hocon:$configurateVersion")
  implementation("com.squareup.okio:okio:$okioVersion")
  implementation("org.spigotmc:spigot-api:$spigotVersion")
  implementation("com.mashape.unirest:unirest-java:$unirestVersion")
}
tasks {
  shadowJar {
    val jarName = "Catalyst-Bungee-${project.version}.jar"
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
      include(dependency("com.squareup.okio:okio:$okioVersion"))
      include(dependency("com.mashape.unirest:unirest-java:$unirestVersion"))
      include(dependency("net.sf.trove4j:trove4j:$troveVersion"))
      include(dependency("com.mashape.unirest:unirest-java:$unirestVersion"))
    }
  }
}

artifacts {
  archives(tasks.shadowJar)
}

