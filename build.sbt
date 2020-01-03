name := "petehazell-raml2md"
organization := "peterhazell"

version := "1.0-SNAPSHOT"

resolvers += "MuleSoftReleases" at "https://repository.mulesoft.org/nexus/content/repositories/public/"

lazy val root = (project in file("."))
  .enablePlugins(PlayScala)
  .settings(
    name := "petehazell-raml2md",
    organization := "peterhazell",
    version := "1.0-SNAPSHOT",
    scalaVersion := "2.12.6",
    PlayKeys.playDefaultPort := 7242,
    libraryDependencies ++= Seq(
      "org.scalatestplus.play" %% "scalatestplus-play" % "4.0.3" % Test,
      "org.mockito" % "mockito-all" % "1.10.19",
      "org.raml" % "webapi-parser" % "0.2.0",
      "com.kotcrab.remark" % "remark" % "1.0.0",
      guice
    )
  )
