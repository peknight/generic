import com.peknight.build.gav.*
import com.peknight.build.sbt.*

commonSettings

lazy val generic = (project in file("."))
  .settings(name := "generic")
  .aggregate(
    genericCore.jvm,
    genericCore.js,
    genericCore.native,
    genericScalaCheck.jvm,
    genericScalaCheck.js,
    genericMigration.jvm,
    genericMigration.js,
    genericMonocle.jvm,
    genericMonocle.js,
  )

lazy val genericCore = (crossProject(JVMPlatform, JSPlatform, NativePlatform) in file("generic-core"))
  .settings(name := "generic-core")
  .settings(crossDependencies(typelevel.cats))

lazy val genericScalaCheck = (crossProject(JVMPlatform, JSPlatform) in file("generic-scalacheck"))
  .dependsOn(genericCore)
  .settings(
    name := "generic-scalacheck",
    scalacOptions --= Seq(
      "-Werror",
    ),
  )
  .settings(crossDependencies(scalaCheck, peknight.cats.scalaCheck))
  .settings(crossTestDependencies(
    peknight.cats,
    typelevel.cats.laws,
  ))

lazy val genericMigration = (crossProject(JVMPlatform, JSPlatform) in file("generic-migration"))
  .dependsOn(
    genericCore,
    genericScalaCheck % Test
  )
  .settings(
    name := "generic-migration",
    scalacOptions --= Seq(
      "-Werror",
    ),
  )
  .settings(crossTestDependencies(peknight.cats))

lazy val genericMonocle = (crossProject(JVMPlatform, JSPlatform) in file("generic-monocle"))
  .dependsOn(
    genericCore,
    genericScalaCheck % Test,
  )
  .settings(
    name := "generic-monocle",
    scalacOptions --= Seq(
      "-Xfatal-warnings",
    ),
  )
  .settings(crossDependencies(optics.monocle))
