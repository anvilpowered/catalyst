rootProject.name = "Catalyst"

include(":catalyst-api")
include(":catalyst-common")
include(":catalyst-velocity")

project(":catalyst-api").projectDir = File("api")
project(":catalyst-common").projectDir = File("common")
project(":catalyst-velocity").projectDir = File("velocity")

