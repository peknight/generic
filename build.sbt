ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.2.1"

ThisBuild / organization := "com.peknight"

lazy val commonSettings = Seq(
  scalacOptions ++= Seq(
    "-feature",
    "-deprecation",
    "-unchecked",
    "-Xfatal-warnings",
    "-language:strictEquality",
    "-Xmax-inlines:64"
  ),
)

lazy val generic = (project in file("."))
  .aggregate(
    genericCore.jvm,
    genericCore.js,
    genericScalaCheck.jvm,
    genericScalaCheck.js,
    genericMigration.jvm,
    genericMigration.js
  )
  .enablePlugins(JavaAppPackaging)
  .settings(commonSettings)
  .settings(
    name := "generic",
  )

lazy val genericCore = (crossProject(JSPlatform, JVMPlatform) in file("generic-core"))
  .settings(commonSettings)
  .settings(
    name := "generic-core",
    libraryDependencies ++= Seq(
      "org.typelevel" %%% "cats-core" % catsVersion,
    ),
  )

lazy val genericScalaCheck = (crossProject(JSPlatform, JVMPlatform) in file("generic-scalacheck"))
  .settings(commonSettings)
  .dependsOn(genericCore)
  .settings(
    name := "generic-scalacheck",
    libraryDependencies ++= Seq(
      "org.scalacheck" %%% "scalacheck" % scalaCheckVersion,
      "com.peknight" %%% "cats-instances-scalacheck" % pekCatsInstancesScalaCheckVersion,
    ),
    scalacOptions --= Seq(
      "-Xfatal-warnings",
    ),
  )

lazy val genericMigration = (crossProject(JSPlatform, JVMPlatform) in file("generic-migration"))
  .settings(commonSettings)
  .dependsOn(genericCore, genericScalaCheck % Test)
  .settings(
    name := "generic-migration",
  )

val catsVersion = "2.9.0"
val scalaCheckVersion = "1.17.0"
val pekCatsInstancesScalaCheckVersion = "0.1.0-SNAPSHOT"
