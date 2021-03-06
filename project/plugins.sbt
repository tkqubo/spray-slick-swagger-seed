logLevel := Level.Warn

addSbtPlugin("com.timushev.sbt" % "sbt-updates" % "0.1.9")
addSbtPlugin("io.spray" % "sbt-revolver" % "0.7.2")
addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.14.0")
addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "3.0.0")
addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.0.6")
addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.3.3")
addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "0.8.0")
addSbtPlugin("org.flywaydb" % "flyway-sbt" % "3.2.1")
//addSbtPlugin("org.scoverage" % "sbt-coveralls" % "1.0.0")
lazy val root = project.in(file(".")).dependsOn(githubRepo)
lazy val githubRepo = uri("git://github.com/scoverage/sbt-coveralls.git")

resolvers += "Flyway" at "http://flywaydb.org/repo"
