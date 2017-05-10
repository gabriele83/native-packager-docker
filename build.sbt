/**
  * Start SBT with env system properties and use these in docker publish command
  *
  * sbt -Denv=dev docker:publish
  * sbt -Denv=test docker:publish
  * sbt -Denv=prod docker:publish
  *
  */

import BuildEnvPlugin.autoImport.BuildEnv
import com.typesafe.sbt.packager.docker.{Cmd, ExecCmd}
import sbt.Keys.mappings

enablePlugins(DockerPlugin, JavaAppPackaging)


val projectName = "native-packager-docker"
val dockerRepo = """to_be_defined"""

name := projectName
organization := "it.gabriele"
version := "0.1.0"
libraryDependencies += "com.typesafe" % "config" % "1.3.1"
scalaVersion := "2.12.2"


mappings in Universal += {
  // logic like this belongs into an AutoPlugin
  val confFile = buildEnv.value match {
    case BuildEnv.Development => "dev.conf"
    case BuildEnv.Test => "test.conf"
    case BuildEnv.Production => "prod.conf"
  }
  ((resourceDirectory in Compile).value / confFile) -> "conf/application.conf"
}
javaOptions in Universal += "-Dconfig.file=conf/application.conf"

packageName in Docker := s"${buildEnv.value}-${projectName}".toLowerCase
dockerUpdateLatest := true
dockerRepository := Some(dockerRepo)
dockerCommands := Seq(
  Cmd("FROM", "java:latest"),
  Cmd("ENV", """JAVA_OPTS='-Xmx1g'"""),
  Cmd("WORKDIR", "/opt/docker"),
  Cmd("ADD", "opt", "/opt"),
  ExecCmd("RUN", "chown", "-R", "daemon:daemon", "."),
  ExecCmd("RUN", "apt-get", "update"),
  Cmd("USER", "daemon"),
  ExecCmd("ENTRYPOINT", s"bin/${projectName}"),
  ExecCmd("CMD"))




fork in run := true
parallelExecution in Test := false