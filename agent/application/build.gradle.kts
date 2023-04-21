plugins {
    id("kotlin-jvm.base-conventions")
}

dependencies {
    commonMainApi(project(":catalyst-core-domain"))
    commonMainImplementation(libs.kbrig.core)
    commonMainImplementation(libs.anvil.agent.application)
}
