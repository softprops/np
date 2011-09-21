sbtPlugin := true

organization := "me.lessis"

name := "np"

version <<= sbtVersion(v => "0.1.1-%s-SNAPSHOT" format(v))

publishTo :=  Some(Resolver.file("lessis repo", new java.io.File("/var/www/repo")))

seq(ScriptedPlugin.scriptedSettings:_*)
