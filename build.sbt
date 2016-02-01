Seq(Revolver.settings: _*)

mainClass in compile := Some("com.github.qubo.seed.Boot")

val environment = Option(System.getProperty("environment")).getOrElse("localhost")

javaOptions in Global ++= Seq(
  s"-Djava.util.Arrays.useLegacyMergeSort=true"
)
javaOptions in Global ++= Seq(
  s"-Denvironment=$environment",
  s"-Dconfig.file=${Option(System.getProperty("config.file")).getOrElse(s"src/main/resources/$environment.conf")}"
)
javaOptions in Test ++= Seq(
  s"-Denvironment=test",
  s"-Dconfig.file=${Option(System.getProperty("config.file")).getOrElse("src/main/resources/test.conf")}"
)

mainClass in assembly := Some("com.github.qubo.seed.Boot")
test in assembly := {}

aggregate in assembly := false
assemblyMergeStrategy in assembly := {
  case x if Assembly.isConfigFile(x) => MergeStrategy.concat
  case ".DS_Store" => MergeStrategy.discard
  case PathList(ps @ _*) if Assembly.isReadme(ps.last) || Assembly.isLicenseFile(ps.last) => MergeStrategy.rename
  case PathList("config", xs @ _*) => MergeStrategy.first
  case PathList("logback", xs @ _*) => MergeStrategy.first
  case _ => MergeStrategy.deduplicate
}
