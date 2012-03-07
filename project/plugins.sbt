libraryDependencies <++= (sbtVersion) { sv => Seq(
  "org.scala-tools.sbt" %% "scripted-plugin" % sv
  )
}

addSbtPlugin("me.lessis" % "ls-sbt" % "0.1.1")

addSbtPlugin("com.jsuereth" % "xsbt-gpg-plugin" % "0.6")

//resolvers += Resolver.url("Typesafe repository", new java.net.URL("http://typesafe.artifactoryonline.com/typesafe/ivy-releases/"))(Resolver.defaultIvyPatterns)

