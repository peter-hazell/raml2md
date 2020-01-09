resolvers += "MuleSoftReleases" at "https://repository.mulesoft.org/nexus/content/repositories/public/"

lazy val root = (project in file("."))
  .enablePlugins(PlayScala)
  .settings(
    name := "raml2md",
    organization := "com.petehazell",
    version := "1.0-SNAPSHOT",
    scalaVersion := "2.12.6",
    PlayKeys.playDefaultPort := 9001,
    libraryDependencies ++= Seq(
      "org.scalatestplus.play" %% "scalatestplus-play" % "4.0.3" % Test,
      "org.mockito"            % "mockito-all"         % "1.10.19",
      "org.raml"               % "webapi-parser"       % "0.2.0",
      "com.kotcrab.remark"     % "remark"              % "1.0.0",
      "org.webjars"            % "bootstrap"           % "4.4.1",
      "org.webjars"            % "jquery"              % "3.4.1",
      guice,
      ws
    )
  )
