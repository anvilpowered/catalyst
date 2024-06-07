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

    compileOnly(platform(libs.adventure.bom))
    compileOnly("net.kyori:adventure-text-serializer-legacy")
    compileOnly("net.kyori:adventure-text-serializer-plain")

    implementation(project(":catalyst-api"))
    implementation(project(":catalyst-core"))
    implementation(libs.anvil.velocity)
    kapt(libs.velocity)
    implementation(libs.discord.jda) {
        exclude(group = "org.slf4j")
    }
    implementation(libs.discord.webhooks) {
        exclude(group = "org.slf4j")
    }
}
