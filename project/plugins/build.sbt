libraryDependencies <++= (sbtVersion) { sv => Seq(
  //"net.databinder" %% "posterous-sbt" % ("0.3.0_sbt" + sv)
  "org.scala-tools.sbt" %% "scripted-plugin" % sv
  )
}

resolvers += Resolver.url("Typesafe repository", new java.net.URL("http://typesafe.artifactoryonline.com/typesafe/ivy-releases/"))(Resolver.defaultIvyPatterns)
