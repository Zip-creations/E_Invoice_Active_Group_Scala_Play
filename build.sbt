import org.scalajs.sbtplugin.ScalaJSPlugin
import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._

// version := "1.0-SNAPSHOT"
ThisBuild / scalaVersion := "3.7.1"

lazy val scalaJS = (project in file("ScalaJS")).enablePlugins(ScalaJSPlugin).settings(
    name := "ScalaJS",
    scalaJSUseMainModuleInitializer := true,
    Compile / fullOptJS / artifactPath := baseDirectory.value / "../public/javascripts/scalaJSmain.js"
    )

lazy val backend = (project in file(".")).enablePlugins(PlayScala).settings(
    name := """E-Rechnung Active Group""",
    libraryDependencies += guice,
    libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "7.0.1" % Test,
    libraryDependencies += "org.typelevel" %% "cats-core" % "2.13.0",
    libraryDependencies += "org.scalameta" %% "munit" % "1.1.1" % Test
    ).dependsOn(scalaJS).settings(
        Compile / compile := (Compile/compile).dependsOn(scalaJS / Compile / fullOptJS).value
    )

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.example.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.example.binders._"
