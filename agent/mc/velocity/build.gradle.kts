plugins {
    id("kotlin-jvm.base-conventions")
    kotlin("kapt")
}

dependencies {
    commonMainApi(project(":catalyst-agent-application"))
    commonMainCompileOnly(libs.anvil.agent.application)
    jvmMainImplementation(libs.velocity) {
//        exclude("com.velocitypowered", "velocity-brigadier")
    }
    kapt(libs.velocity)
}
