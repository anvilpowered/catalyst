dependencies {
    api(project(":catalyst-api"))
    runtimeOnly(libs.driver.mariadb)
    runtimeOnly(libs.driver.postgresql)
}
