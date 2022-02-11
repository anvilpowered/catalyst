import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

val brigadierVersion: String by project

plugins {
    kotlin("jvm")
    id("net.kyori.blossom") version "1.3.0"
    id("org.jetbrains.kotlin.kapt")
}

dependencies {
    api(project(":catalyst-api"))
    api(kotlin("reflect"))
    api("net.md-5:brigadier:$brigadierVersion")
}


blossom {
    replaceToken("{modVersion}", version)
    val format = SimpleDateFormat("yyyy-MM-dd-HH:mm:ss z")
    format.timeZone = TimeZone.getTimeZone("UTC")
    val buildDate = format.format(Date())
    replaceToken("{buildDate}", buildDate)
}
