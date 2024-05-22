plugins {
    alias(libs.plugins.kotlin.serialization)
    kotlin("kapt")
}
dependencies {
    api(libs.anvil.core)

    api(platform(libs.exposed.bom))
    api(libs.bundles.exposed) {
        exclude(group = "org.slf4j")
    }

    api(platform(libs.adventure.bom))
    api("net.kyori:adventure-text-serializer-legacy")
    api("net.kyori:adventure-text-serializer-plain")

    implementation(project(":catalyst-api"))
    implementation(project(":catalyst-core"))
    implementation(libs.anvil.velocity)
    kapt(libs.velocity)
    implementation(libs.discord.jda)
    implementation(libs.discord.webhooks)
}
