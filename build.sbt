dockerfile in docker := {
  val appDir = stage.value
  val targetDir = "/app"

  new Dockerfile {
    from("java:8")
    maintainer("Paulo R. A. Sales <salespaulo@gmail.com>")
    expose(8080)
    entryPoint(s"$targetDir/bin/${executableScriptName.value}")
    copy(appDir, targetDir)
  }
}

buildOptions in docker := BuildOptions(cache = false)

lazy val `simple-api` = (project in file(".")).
  enablePlugins(JavaAppPackaging, sbtdocker.DockerPlugin).
  settings(
    organization := "simple.api",
    name         := "simple-api",
    version      := "0.0.1",
    scalaVersion := "2.11.8",
    libraryDependencies ++= Seq(
      "org.scalacheck"     %% "scalacheck"    % "1.13.2"  % "test",
      "com.github.finagle" %% "finch-core"    % "0.10.0"          ,
      "com.github.finagle" %% "finch-circe"   % "0.10.0"          ,
      "io.circe"           %% "circe-generic" % "0.4.1"           ,
      "io.getquill"        %% "quill-jdbc"    % "0.8.0"           ,
      "org.postgresql"     %  "postgresql"    % "9.4.1208"
    )
  )
