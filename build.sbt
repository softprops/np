sbtPlugin := true

organization := "me.lessis"

name := "np"

version <<= sbtVersion(v => "0.1.0-%s-SNAPSHOT" format(v))
