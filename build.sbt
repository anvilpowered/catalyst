ThisBuild / version := "0.4.0-SNAPSHOT"
ThisBuild / scalaVersion := "3.8.1"
ThisBuild / scalacOptions ++= Seq(
  "-Wnonunit-statement",
  "-deprecation",
)

ThisBuild / resolvers ++= Seq(
  "papermc" at "https://repo.papermc.io/repository/maven-public/",
  "sponge" at "https://repo.spongepowered.org/repository/maven-public/",
)

ThisBuild / libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-effect" % "3.6.3",
  "org.typelevel" %% "log4cats-slf4j" % "2.7.1",
) ++ Seq(
  "fs2-core",
  "fs2-io",
).map("co.fs2" %% _ % "3.12.2") ++ Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser",
).map(_ % "0.14.15") ++ Seq(
  "http4s-core",
  "http4s-client",
  "http4s-server",
  "http4s-dsl",
  "http4s-circe",
).map("org.http4s" %% _ % "0.23.33")

lazy val root = (project in file("."))
  .settings(
    name := "catalyst",
  )

lazy val libdomain = (project in file("lib/domain"))
  .settings(
    name := "domain",
    libraryDependencies ++= Seq(
      "org.tpolecat" %% "skunk-core" % "0.6.5",
    ),
  )

lazy val libcommand = (project in file("lib/command"))
  .settings(
    name := "command",
  )

lazy val apiGame = (project in file("api/game"))
  .settings(
    name := "catalyst-api-game",
    libraryDependencies ++= Seq(
      "org.anvilpowered" %% "anvil-platform" % "0.4.0-SNAPSHOT",
      "net.luckperms" % "api" % "5.5",
    ) ++ Seq(
      "net.kyori" % "adventure-api",
      "net.kyori" % "adventure-text-minimessage",
    ).map(_ % "4.26.1") ++ Seq(
      "org.spongepowered" % "configurate-core",
      "org.spongepowered" % "configurate-hocon",
      "org.spongepowered" % "configurate-yaml",
    ).map(_ % "4.2.0"),
  )

lazy val appGameCore = (project in file("app/game/core"))
  .dependsOn(apiGame)

lazy val appGamePaper = (project in file("app/game/paper"))
  .dependsOn(appGameCore)

lazy val appGameSponge = (project in file("app/game/sponge"))
  .settings(
    name := "catalyst-game-sponge",
    libraryDependencies ++= Seq(
      "org.spongepowered" % "spongeapi" % "17.0.0"
    ),
  )
  .dependsOn(appGameCore)
