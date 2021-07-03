import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

val brigadierVersion: String by project

plugins {
  java
  id("net.kyori.blossom") version "1.3.0"
}

dependencies {
  implementation(project(":catalyst-api"))
  implementation("net.md-5:brigadier:$brigadierVersion")
}

blossom {
  replaceToken("\$modVersion", version)
  val format = SimpleDateFormat("yyyy-MM-dd-HH:mm:ss z")
  format.timeZone = TimeZone.getTimeZone("UTC")
  val buildDate = format.format(Date())
  replaceToken("\$buildDate", buildDate)
}
