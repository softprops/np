sbtPlugin := true

organization := "me.lessis"

name := "np"

version <<= sbtVersion { v =>
  if(v.startsWith("0.11")) "0.2.0"
  else error("unsupported version of sbt %s" format v)
}

seq(ScriptedPlugin.scriptedSettings:_*)

seq(lsSettings:_*)

(LsKeys.tags in LsKeys.lsync) := Seq("sbt")

//(externalResolvers in LsKeys.lsync) := Seq("less is" at "http://repo.lessis.me")

homepage :=
  Some(url("https://github.com/softprops/np"))

description :=
  "Generates sbt project source structures"

licenses <<= (version)(Seq(
  v => ("MIT", url("https://github.com/softprops/np/blob/%s/LICENSE".format(v)))
))

publishTo := Some(Resolver.url("sbt-plugin-releases", url(
  "http://scalasbt.artifactoryonline.com/scalasbt/sbt-plugin-releases/"
))(Resolver.ivyStylePatterns))

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
