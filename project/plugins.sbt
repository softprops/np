libraryDependencies <++= (sbtVersion) { sv => Seq(
  "org.scala-tools.sbt" %% "scripted-plugin" % sv
  )
}

//resolvers += Resolver.url("Typesafe repository", new java.net.URL("http://typesafe.artifactoryonline.com/typesafe/ivy-releases/"))(Resolver.defaultIvyPatterns)

