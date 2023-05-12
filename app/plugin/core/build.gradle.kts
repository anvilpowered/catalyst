plugins {
    id("kotlin-jvm.base-conventions")
}

dependencies {
    commonMainApi(project(":catalyst-domain"))
    commonMainApi(libs.kbrig.core)
    commonMainApi(libs.anvil.api)
}
