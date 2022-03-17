plugins {
    `java-library`
    kotlin("jvm")
}

val anvilVersion: String by project
val jdaVersion: String by project
val luckpermsVersion: String by project
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

dependencies {
    compileOnlyApi("com.google.inject:guice:5.0.1")
    compileOnlyApi("org.anvilpowered:anvil-api:0.4-SNAPSHOT")
    api("net.dv8tion:JDA:$jdaVersion")
    compileOnlyApi("net.luckperms:api:$luckpermsVersion")
    api("org.apache.httpcomponents:httpasyncclient:$apacheAsyncVersion")
    api("org.apache.httpcomponents:httpclient:$apacheClientVersion")
    api("org.apache.httpcomponents:httpcore:$apacheCoreVersion")
    api("org.apache.httpcomponents:httpmime:$apacheMimeVersion")
    api("org.apache.httpcomponents:httpcore-nio:$apacheNioVersion")
    api("com.fasterxml.jackson.core:jackson-annotations:$jacksonVersion")
    api("com.fasterxml.jackson.core:jackson-core:$jacksonVersion")
    api("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
    api("org.json:json:$jsonVersion")
    api("net.kyori:event-method-asm:$kyoriVersion")
    api("net.kyori:event-method-asm:$kyoriVersion")
    compileOnlyApi("net.kyori:adventure-api:4.5.1")
    compileOnlyApi("net.kyori:adventure-text-serializer-legacy:4.9.3")
    compileOnlyApi("net.kyori:adventure-text-serializer-plain:4.9.3")
    api("net.kyori:adventure-text-feature-pagination:4.0.0-SNAPSHOT")
    api("com.squareup.okio:okio:$okioVersion")
    compileOnlyApi("org.spongepowered:configurate-core:4.1.2")
    compileOnlyApi("org.spongepowered:configurate-hocon:4.1.2")
    api("com.mashape.unirest:unirest-java:$unirestVersion")
}
