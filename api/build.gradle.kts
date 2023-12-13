plugins {
    id("catalyst-publish")
}

dependencies {
    compileOnlyApi(libs.luckperms)
    api(libs.anvil.velocity)
    api(libs.kotlinx.serialization)
}
