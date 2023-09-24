plugins {
    kotlin("kapt")
}

dependencies {
    implementation(project(":catalyst-app-plugin-core"))
    compileOnly(libs.velocity)
    kapt(libs.velocity)
    implementation(libs.kbrig.brigadier)
}
