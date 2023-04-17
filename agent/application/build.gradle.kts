plugins {
    id("kotlin-jvm.base-conventions")
}

dependencies {
    commonMainImplementation(libs.kbrig.core)
    commonMainCompileOnly(libs.anvil.agent.application)
}
