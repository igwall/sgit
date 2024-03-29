import Dependencies._

ThisBuild / scalaVersion := "2.13.0"
ThisBuild / version := "1.0"
ThisBuild / organization := "com.example"
ThisBuild / organizationName := "example"

lazy val root = (project in file("."))
  .settings(
    name := "sgit",
    libraryDependencies += scalaTest % Test
  )

import sbtassembly.AssemblyPlugin.defaultUniversalScript

assemblyOption in assembly := (assemblyOption in assembly).value
  .copy(prependShellScript = Some(defaultUniversalScript(shebang = false)))
assemblyJarName in assembly := s"${name.value}-${version.value}"

parallelExecution in Test := false
scalacOptions ++= Seq(
  "-Xlint:unused"
)
