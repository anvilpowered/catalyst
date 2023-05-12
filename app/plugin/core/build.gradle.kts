plugins {
    id("kotlin-jvm.base-conventions")
}

dependencies {
    commonMainApi(project(":catalyst-domain"))
    commonMainImplementation(libs.kbrig.core)
    jvmMainImplementation(libs.anvil.api)
}
