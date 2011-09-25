sbtPlugin := true

organization := "me.lessis"

name := "np"

version <<= sbtVersion { v =>
  if(v.startsWith("0.10")) "0.2.0-%s-SNAPSHOT".format(v)
  else if(v.startsWith("0.11")) "0.2.0-SNAPSHOT"
  else error("unsupported version of sbt %s" format v)
}

publishTo :=  Some(Resolver.file("lessis repo", new java.io.File("/var/www/repo")))

seq(ScriptedPlugin.scriptedSettings:_*)
