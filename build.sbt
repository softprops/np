sbtPlugin := true

organization := "me.lessis"

name := "np"

version <<= sbtVersion(v => "0.1.0-%s" format(v))

publishTo :=  Some(Resolver.file("lessis repo", new java.io.File("/var/www/repo")))

