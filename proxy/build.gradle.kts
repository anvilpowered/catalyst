plugins {
    alias(libs.plugins.kotlin.serialization)
    kotlin("kapt")
}
dependencies {
    api(libs.anvil.core)
    api(platform("net.kyori:adventure-bom:4.14.0"))
    api("net.kyori:adventure-text-serializer-legacy")
    api("net.kyori:adventure-text-serializer-plain")

    implementation(project(":catalyst-api"))
    implementation(project(":catalyst-core"))
    implementation(libs.anvil.velocity)
    kapt(libs.velocity)
    implementation(libs.discord.jda)
    implementation(libs.discord.webhooks)
}
