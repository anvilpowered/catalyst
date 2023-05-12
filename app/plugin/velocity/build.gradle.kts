plugins {
    id("kotlin-jvm.base-conventions")
    kotlin("kapt")
}

dependencies {
    commonMainImplementation(project(":catalyst-app-plugin-core"))
    commonMainImplementation(project(":catalyst-infrastructure-game-velocity"))
    jvmMainCompileOnly(libs.velocity)
    kapt(libs.velocity)
    commonMainImplementation(libs.kbrig.brigadier)
}
