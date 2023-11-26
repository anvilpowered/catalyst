plugins {
    alias(libs.plugins.kotlin.serialization)
    kotlin("kapt")
}
dependencies {
    api(libs.anvil.core)
    api(platform("net.kyori:adventure-bom:4.14.0"))
    api("net.kyori:adventure-text-serializer-legacy")
    api("net.kyori:adventure-text-serializer-plain")

    implementation(project(":catalyst-api-velocity"))
    implementation(project(":catalyst-core"))
    implementation(libs.anvil.velocity)
    kapt(libs.velocity)
    implementation(libs.discord.jda)
    implementation(libs.discord.webhooks)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.content.negotiation)
    api(libs.ktor.serialization)
    api(libs.koin)
    runtimeOnly(libs.postgrsql)
//    implementation("net.kyori:adventure-text-minimessage:$minimessageVersion")
//    compileOnly("com.velocitypowered:velocity-api:3.0.0-SNAPSHOT")
//    kapt("com.velocitypowered:velocity-api:3.0.0-SNAPSHOT")
}
