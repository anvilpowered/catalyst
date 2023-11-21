plugins {
    alias(libs.plugins.kotlin.serialization)
}
dependencies {
    implementation(project(":catalyst-api-velocity"))
    implementation(project(":catalyst-core"))
    implementation(libs.anvil.velocity)
    api(libs.jda)
    api(libs.ktor.client.cio)
    api(libs.ktor.serialization)
//    implementation("net.kyori:adventure-text-minimessage:$minimessageVersion")
//    compileOnly("com.velocitypowered:velocity-api:3.0.0-SNAPSHOT")
//    kapt("com.velocitypowered:velocity-api:3.0.0-SNAPSHOT")
}
