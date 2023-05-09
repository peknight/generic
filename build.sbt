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
    genericMapper.jvm,
    genericMapper.js,
    genericDoobie,
    genericHttp4s.jvm,
    genericHttp4s.js,
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

lazy val genericMapper = (crossProject(JSPlatform, JVMPlatform) in file("generic-mapper"))
  .dependsOn(genericCore, genericScalaCheck % Test)
  .settings(commonSettings)
  .settings(
    name := "generic-mapper",
    scalacOptions --= Seq(
      "-Xfatal-warnings",
    ),
    libraryDependencies ++= Seq(
      "com.peknight" %%% "cats-instances-tuple" % pekCatsInstancesVersion % Test,
    ),
  )

lazy val genericDoobie = (project in file("generic-doobie"))
  .dependsOn(genericMapper.jvm)
  .settings(commonSettings)
  .settings(
    name := "generic-doobie",
    libraryDependencies ++= Seq(
      doobieCore,
    ),
  )

lazy val genericHttp4s = (crossProject(JSPlatform, JVMPlatform) in file("generic-http4s"))
  .dependsOn(genericMapper)
  .settings(commonSettings)
  .settings(
    name := "generic-http4s",
    libraryDependencies ++= Seq(
      "org.http4s" %%% "http4s-core" % http4sVersion,
    ),
  )

val catsVersion = "2.9.0"
val doobieVersion = "1.0.0-RC2"
val http4sVersion = "1.0.0-M32"
val scalaCheckVersion = "1.17.0"
val pekCatsInstancesVersion = "0.1.0-SNAPSHOT"

val doobieCore = "org.tpolecat" %% "doobie-core" % doobieVersion
