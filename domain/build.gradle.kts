plugins {
    id("kotlin-jvm.base-conventions")
}

dependencies {
    commonMainCompileOnly(libs.anvil.domain)
}
