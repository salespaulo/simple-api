
lazy val `simple-api` = (project in file(".")).
  settings(
    organization := "ps.simple.api",
    name         := "simple-api",
    version      := "0.0.1",
    scalaVersion := "2.11.8",
    libraryDependencies ++= Seq(
      "org.scalacheck"     %% "scalacheck"    % "1.13.2" % "test",
      "com.github.finagle" %% "finch-core"    % "0.10.0",
      "com.github.finagle" %% "finch-circe"   % "0.10.0",
      "io.circe"           %% "circe-generic" % "0.4.1",
      "io.getquill"        %% "quill-jdbc"    % "0.8.0",
      "org.postgresql"     %  "postgresql"    % "9.4.1208"
    )
  )
