version = ""

val anvilVersion: String by project
val jdaVersion: String by project
val luckpermsVersion: String by project

dependencies {
    api("org.anvilpowered:anvil-api:$anvilVersion")
    api("net.dv8tion:JDA:$jdaVersion")
    api("net.luckperms:api:$luckpermsVersion")
}
