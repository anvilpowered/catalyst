dependencies {
    api(project(":catalyst-api"))
    api(libs.jda)
    api(libs.ktor.client.cio)
    api("net.kyori:adventure-text-serializer-legacy")
    api("net.kyori:adventure-text-serializer-plain")
}
