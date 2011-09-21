sbtPlugin := true

organization := "com.bar"

name := "foo"

version <<= sbtVersion("0.1.0-%s-SNAPSHOT" format _)
