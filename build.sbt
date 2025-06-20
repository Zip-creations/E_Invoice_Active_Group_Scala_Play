import org.scalajs.sbtplugin.ScalaJSPlugin
import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._

// See https://github.com/jkutner/play-with-scalajs-example/tree/master

// version := "1.0-SNAPSHOT"
ThisBuild / scalaVersion := "3.7.1"

lazy val shared = (project in file("shared")).enablePlugins(ScalaJSPlugin).settings(
    name := "shared",
    libraryDependencies += "org.typelevel" %% "cats-core" % "2.13.0",
    scalaJSUseMainModuleInitializer := false
)

lazy val scalaJS = (project in file("ScalaJS")).enablePlugins(ScalaJSPlugin).settings(
    name := "ScalaJS",
    scalaJSUseMainModuleInitializer := true,
    libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "2.8.0",
    Compile / fullOptJS / artifactPath := baseDirectory.value / "../public/javascripts/scalaJSmain.js"
    ).dependsOn(shared)

lazy val backend = (project in file(".")).enablePlugins(PlayScala).settings(
    name := """E-Rechnung Active Group""",
    libraryDependencies += guice,
    libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "7.0.1" % Test,
    libraryDependencies += "org.scalameta" %% "munit" % "1.1.1" % Test
    ).dependsOn(scalaJS).settings(
        Compile / compile := (Compile/compile).dependsOn(scalaJS / Compile / fullOptJS).value
    )
