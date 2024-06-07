dependencies {
    api(project(":catalyst-api"))
    runtimeOnly(libs.driver.mariadb) {
        exclude(group = "org.slf4j")
    }
    runtimeOnly(libs.driver.postgresql)
}
