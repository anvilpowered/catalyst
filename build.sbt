ThisBuild / version := "0.4.0-SNAPSHOT"
ThisBuild / scalaVersion := "3.8.1"
ThisBuild / scalacOptions ++= Seq(
  "-Wnonunit-statement",
  "-deprecation",
)

lazy val root = (project in file("."))
  .settings(
    name := "catalyst",
  )

lazy val apiGame = (project in file("api/game"))
  .settings(
    name := "catalyst-api-game",
    libraryDependencies ++= Seq(
      "org.anvilpowered" %% "anvil-core" % "0.4.0-SNAPSHOT",
      "org.typelevel" %% "cats-effect" % "3.6.3",
      "org.typelevel" %% "log4cats-slf4j" % "2.7.1",
      "org.tpolecat" %% "skunk-core" % "0.6.5",
      "net.luckperms" % "api" % "5.5",
    ) ++ Seq(
      "net.kyori" % "adventure-api",
      "net.kyori" % "adventure-text-minimessage",
    ).map(_ % "4.26.1") ++ Seq(
      "org.spongepowered" % "configurate-core",
      "org.spongepowered" % "configurate-hocon",
      "org.spongepowered" % "configurate-yaml",
    ).map(_ % "4.2.0") ++ Seq(
      "io.circe" %% "circe-core",
      "io.circe" %% "circe-generic",
      "io.circe" %% "circe-parser",
    ).map(_ % "0.14.15") ++ Seq(
      "co.fs2" %% "fs2-core",
      "co.fs2" %% "fs2-io",
    ).map(_ % "3.12.2"),
  )

lazy val appGameCore = (project in file("app/game/core"))
  .dependsOn(apiGame)

lazy val appGamePaper = (project in file("app/game/paper"))
  .dependsOn(appGameCore)
