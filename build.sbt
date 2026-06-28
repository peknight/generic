import com.peknight.build.gav.*
import com.peknight.build.sbt.*

commonSettings

ThisBuild / evictionErrorLevel := Level.Warn
ThisBuild / scalacOptions --= Seq("-Werror", "-Xfatal-warnings")

lazy val generic = (project in file("."))
  .settings(name := "generic")
  .aggregate(genericCore.projectRefs *)
  .aggregate(genericScalaCheck.projectRefs *)
  .aggregate(genericMigration.projectRefs *)
  .aggregate(genericMonocle.projectRefs *)

lazy val genericCore = (projectMatrix in file("generic-core"))
  .settings(name := "generic-core")
  .settings(libraryDependencies ++= dependencies(typelevel.cats))
  .jvmPlatform(scalaVersions = Seq(scala.scala3.version))
  .jsPlatform(scalaVersions = Seq(scala.scala3.version))
  .nativePlatform(scalaVersions = Seq(scala.scala3.version))

lazy val genericScalaCheck = (projectMatrix in file("generic-scalacheck"))
  .dependsOn(genericCore)
  .settings(name := "generic-scalacheck")
  .settings(libraryDependencies ++= dependencies(
    scalaCheck,
    peknight.cats.scalaCheck,
  ))
  .settings(libraryDependencies ++= testDependencies(
    peknight.cats,
    typelevel.cats.laws,
  ))
  .jvmPlatform(
    scalaVersions = Seq(scala.scala3.version),
    settings = Seq(
      scalacOptions --= Seq("-Werror")
    )
  )
  .jsPlatform(
    scalaVersions = Seq(scala.scala3.version),
    settings = Seq(
      scalacOptions --= Seq("-Werror")
    )
  )
  .nativePlatform(
    scalaVersions = Seq(scala.scala3.version),
    settings = Seq(
      scalacOptions --= Seq("-Werror")
    )
  )

lazy val genericMigration = (projectMatrix in file("generic-migration"))
  .dependsOn(
    genericCore,
    genericScalaCheck % Test,
  )
  .settings(name := "generic-migration")
  .settings(libraryDependencies ++= testDependencies(peknight.cats))
  .jvmPlatform(
    scalaVersions = Seq(scala.scala3.version),
    settings = Seq(
      scalacOptions --= Seq("-Werror")
    )
  )
  .jsPlatform(
    scalaVersions = Seq(scala.scala3.version),
    settings = Seq(
      scalacOptions --= Seq("-Werror")
    )
  )
  .nativePlatform(
    scalaVersions = Seq(scala.scala3.version),
    settings = Seq(
      scalacOptions --= Seq("-Werror")
    )
  )

lazy val genericMonocle = (projectMatrix in file("generic-monocle"))
  .dependsOn(
    genericCore,
    genericScalaCheck % Test,
  )
  .settings(name := "generic-monocle")
  .settings(libraryDependencies ++= dependencies(optics.monocle))
  .jvmPlatform(
    scalaVersions = Seq(scala.scala3.version),
    settings = Seq(
      scalacOptions --= Seq("-Xfatal-warnings")
    )
  )
  .jsPlatform(
    scalaVersions = Seq(scala.scala3.version),
    settings = Seq(
      scalacOptions --= Seq("-Xfatal-warnings")
    )
  )
  .nativePlatform(
    scalaVersions = Seq(scala.scala3.version),
    settings = Seq(
      scalacOptions --= Seq("-Xfatal-warnings")
    )
  )
