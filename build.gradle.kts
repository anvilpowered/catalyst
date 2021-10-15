import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.net.URI

val kotlinVersion: String by project
val apacheLoggingVersion: String by project
val apacheAsyncVersion: String by project
val apacheClientVersion: String by project
val apacheCoreVersion: String by project
val apacheMimeVersion: String by project
val apacheNioVersion: String by project
val emojiVersion: String by project
val jacksonVersion: String by project
val jsonVersion: String by project
val kyoriVersion: String by project
val okioVersion: String by project
val unirestVersion: String by project

plugins {
  id("io.codearte.nexus-staging") version "0.21.2"
  kotlin("jvm") version "1.5.20"
  `maven-publish`
}

ext.set("apiVersion", "0.4.0-SNAPSHOT")

allprojects {
  group = "org.anvilpowered"
  version = "0.4.0-SNAPSHOT"
  apply<JavaLibraryPlugin>()
  apply(plugin = "maven-publish")
  apply(plugin = "signing")
  apply(plugin = "org.jetbrains.kotlin.jvm")

  repositories {
    mavenCentral()
  }

  val compileKotlin: KotlinCompile by tasks
  compileKotlin.kotlinOptions.jvmTarget = "1.8"
}

val _username = if (project.hasProperty("username")) project.findProperty("username") as String else ""
val _password = if (project.hasProperty("password")) project.findProperty("password") as String else ""
val _keyId = if (project.hasProperty("keyId")) project.findProperty("keyId") else ""
val _ringFile = if (project.hasProperty("ringFile")) project.findProperty("ringFile") else ""

subprojects {
  repositories {
    mavenCentral()
    maven("https://jetbrains.bintray.com/xodus")
    maven("https://repo.spongepowered.org/maven")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
  }
  dependencies {
    implementation("org.apache.httpcomponents:httpasyncclient:$apacheAsyncVersion")
    implementation("org.apache.httpcomponents:httpclient:$apacheClientVersion")
    implementation("org.apache.httpcomponents:httpcore:$apacheCoreVersion")
    implementation("org.apache.httpcomponents:httpmime:$apacheMimeVersion")
    implementation("org.apache.httpcomponents:httpcore-nio:$apacheNioVersion")
    implementation("com.vdurmont:emoji-java:$emojiVersion")
    implementation("com.fasterxml.jackson.core:jackson-annotations:$jacksonVersion")
    implementation("com.fasterxml.jackson.core:jackson-core:$jacksonVersion")
    implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
    implementation("org.json:json:$jsonVersion")
    implementation("net.kyori:event-method-asm:$kyoriVersion")
    implementation("net.kyori:event-method-asm:$kyoriVersion")
    implementation("com.squareup.okio:okio:$okioVersion")
    implementation("com.mashape.unirest:unirest-java:$unirestVersion")
  }
  if (project.hasProperty("buildNumber") && (version as String).contains("-SNAPSHOT")) {
    version = (version as String).replace("-SNAPSHOT", "-RC${project.findProperty("buildNumber")}")
  }
  //TODO fix nexusStaging not found issue
/*
  nexusStaging {
    packageGroup = "org.anvilpowered"
    username = _username
    password = _password
  }
*/

  if (project.hasProperty("sign")) {
    ext.set("signing.keyId", _keyId)
    ext.set("signing.password", _password)
    ext.set("signing.secretKeyRingFile", _ringFile)
  }
}

project(":catalyst-api") {
  tasks {
    java {
      withJavadocJar()
      withSourcesJar()
    }
    afterEvaluate {
      publishing {
        repositories {
          maven {
            credentials {
              username = _username
              password = _password
            }
            val releasesRepoUrl = URI("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
            val snapshotsRepoUrl = URI("https://oss.sonatype.org/content/repositories/snapshots")
            url = if ((version as String).endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl
          }
        }
        publications {
          create<MavenPublication>("mavenJava") {
            from(components["java"])
            pom {
              name.set("Catalyst")
              description.set(
                "An essentials plugin for Minecraft proxies that will provide your server with a strong baseline; giving you all the useful " +
                  "commands you need."
              )
              url.set("https://github.com/AnvilPowered/Catalyst")

              scm {
                url.set("https://github.com/AnvilPowered/Catalyst")
                connection.set("scm:git:https://github.com/AnvilPowered/Catalyst.git")
                developerConnection.set("scm:git:https://github.com/AnvilPowered/Catalyst.git")
              }

              licenses {
                license {
                  name.set("GNU LESSER GENERAL PUBLIC LICENSE Version 3")
                  url.set("https://www.gnu.org/licenses/lgpl-3.0.html")
                  distribution.set("repo")
                }
              }
            }
          }
        }
      }
    }
  }
}
