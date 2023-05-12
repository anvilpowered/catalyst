plugins {
    id("kotlin-jvm.base-conventions")
    kotlin("kapt")
}

dependencies {
    commonMainApi(libs.anvil.velocity)
    jvmMainImplementation(libs.velocity) {
        exclude("com.velocitypowered", "velocity-brigadier")
    }
    kapt(libs.velocity)
}
