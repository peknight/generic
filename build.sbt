import com.peknight.build.gav.*
import com.peknight.build.sbt.*

commonSettings

lazy val generic = (project in file("."))
  .aggregate(
    genericCore.jvm,
    genericCore.js,
    genericCore.native,
    genericScalaCheck.jvm,
    genericScalaCheck.js,
    genericScalaCheck.native,
    genericMigration.jvm,
    genericMigration.js,
    genericMigration.native,
    genericMonocle.jvm,
    genericMonocle.js,
    genericMonocle.native,
  )
  .settings(
    name := "generic",
  )

lazy val genericCore = (crossProject(JVMPlatform, JSPlatform, NativePlatform) in file("generic-core"))
  .settings(crossDependencies(typelevel.cats))
  .settings(
    name := "generic-core",
  )

lazy val genericScalaCheck = (crossProject(JVMPlatform, JSPlatform, NativePlatform) in file("generic-scalacheck"))
  .dependsOn(genericCore)
  .settings(crossDependencies(scalaCheck, peknight.instances.cats.scalaCheck))
  .settings(crossDependency(typelevel.cats.laws, Some(Test)))
  .settings(
    name := "generic-scalacheck",
    scalacOptions --= Seq(
      "-Xfatal-warnings",
    ),
  )

lazy val genericMigration = (crossProject(JVMPlatform, JSPlatform, NativePlatform) in file("generic-migration"))
  .dependsOn(
    genericCore,
    genericScalaCheck % Test
  )
  .settings(crossDependency(peknight.instances.cats.tuple, Some(Test)))
  .settings(
    name := "generic-migration",
    scalacOptions --= Seq(
      "-Xfatal-warnings",
    ),
  )

lazy val genericMonocle = (crossProject(JVMPlatform, JSPlatform, NativePlatform) in file("generic-monocle"))
  .dependsOn(
    genericCore,
    genericScalaCheck % Test,
  )
  .settings(crossDependencies(optics.monocle))
  .settings(
    name := "generic-monocle",
    scalacOptions --= Seq(
      "-Xfatal-warnings",
    ),
  )
