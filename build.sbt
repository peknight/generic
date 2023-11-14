ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.1"

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
    genericMigration.js,
    genericMonocle.jvm,
    genericMonocle.js,
    genericInstances,
  )
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
  .dependsOn(genericCore)
  .settings(commonSettings)
  .settings(
    name := "generic-scalacheck",
    libraryDependencies ++= Seq(
      "org.scalacheck" %%% "scalacheck" % scalaCheckVersion,
      "com.peknight" %%% "cats-instances-scalacheck" % pekCatsInstancesVersion,
      "org.typelevel" %%% "cats-laws" % catsVersion % Test,
    ),
    scalacOptions --= Seq(
      "-Xfatal-warnings",
    ),
  )

lazy val genericMigration = (crossProject(JSPlatform, JVMPlatform) in file("generic-migration"))
  .dependsOn(
    genericCore,
    genericScalaCheck % Test
  )
  .settings(commonSettings)
  .settings(
    name := "generic-migration",
    scalacOptions --= Seq(
      "-Xfatal-warnings",
    ),
    libraryDependencies ++= Seq(
      "com.peknight" %%% "cats-instances-tuple" % pekCatsInstancesVersion % Test,
    ),
  )

lazy val genericMonocle = (crossProject(JSPlatform, JVMPlatform) in file("generic-monocle"))
  .dependsOn(
    genericCore,
    genericScalaCheck % Test,
  )
  .settings(commonSettings)
  .settings(
    name := "generic-monocle",
    scalacOptions --= Seq(
      "-Xfatal-warnings",
    ),
    libraryDependencies ++= Seq(
      "dev.optics" %%% "monocle-core" % monocleVersion,
    ),
  )

lazy val genericInstances = (project in file("generic-instances"))
  .aggregate(
    genericInstancesTime.jvm,
    genericInstancesTime.js,
    genericInstancesSquants.jvm,
    genericInstancesSquants.js,
  )
  .settings(commonSettings)
  .settings(
    name := "generic-instances",
  )

lazy val genericInstancesTime = (crossProject(JSPlatform, JVMPlatform) in file("generic-instances/time"))
  .dependsOn(genericMigration)
  .settings(commonSettings)
  .settings(
    name := "generic-instances-time",
    libraryDependencies ++= Seq(
    )
  )

lazy val genericInstancesSquants = (crossProject(JSPlatform, JVMPlatform) in file("generic-instances/squants"))
  .dependsOn(genericMigration)
  .settings(commonSettings)
  .settings(
    name := "generic-instances-squants",
    libraryDependencies ++= Seq(
      "org.typelevel" %%% "squants" % squantsVersion,
    )
  )

val catsVersion = "2.10.0"
val monocleVersion = "3.2.0"
val squantsVersion = "1.8.3"
val scalaCheckVersion = "1.17.0"
val pekVersion = "0.1.0-SNAPSHOT"
val pekCatsInstancesVersion = pekVersion
