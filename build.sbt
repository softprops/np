sbtPlugin := true

organization := "me.lessis"

name := "np"

version <<= sbtVersion { v =>
  if (v.startsWith("0.11") || v.startsWith("0.12") || v.startsWith("0.13")) "0.2.0"
  else error("unsupported version of sbt %s" format v)
}

sbtVersion in Global := "0.13.0-RC4"

scalaVersion in Global := "2.10.2"

scalacOptions += Opts.compile.deprecation

seq(ScriptedPlugin.scriptedSettings:_*)

seq(lsSettings:_*)

(LsKeys.tags in LsKeys.lsync) := Seq("sbt")

homepage :=
  Some(url("https://github.com/softprops/np"))

description :=
  "Generates sbt project source structures"

licenses <<= (version)(v => Seq(
  ("MIT", url("https://github.com/softprops/np/blob/%s/LICENSE".format(v)))
))

publishTo := Some(Classpaths.sbtPluginReleases)

publishMavenStyle := false

publishArtifact in Test := false

pomExtra := (
  <scm>
    <url>git@github.com:softprops/np.git</url>
    <connection>scm:git:git@github.com:softprops/np.git</connection>
  </scm>
  <developers>
    <developer>
      <id>softprops</id>
      <name>Doug Tangren</name>
      <url>https://github.com/softprops</url>
    </developer>
  </developers>
)
