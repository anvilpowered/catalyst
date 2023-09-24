plugins {
    kotlin("kapt")
}

dependencies {
    api(libs.anvil.velocity)
    compileOnly(libs.velocity) {
        exclude("com.velocitypowered", "velocity-brigadier")
    }
    kapt(libs.velocity)
}
