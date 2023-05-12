plugins {
    id("kotlin-jvm.base-conventions")
    alias(libs.plugins.shadow)
}

dependencies {
    commonMainImplementation(project(":catalyst-app-plugin-velocity"))
}
