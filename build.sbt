ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.1.3"

lazy val root = (project in file("."))
  .settings(
    name := "algo-companion"
  )

libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.12"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.12" % "test"
libraryDependencies += "org.scalafx" %% "scalafx" % "16.0.0-R24"

libraryDependencies ++= {
  // Determine OS version of JavaFX binaries
  lazy val osName = System.getProperty("os.name") match {
    case n if n.startsWith("Linux") => "linux"
    case n if n.startsWith("Mac") => "mac"
    case n if n.startsWith("Windows") => "win"
    case _ => throw new Exception("Unknown platform!")
  }
  Seq("base", "controls", "fxml", "graphics", "media", "swing", "web")
    .map(m => "org.openjfx" % s"javafx-$m" % "16" classifier osName)
}