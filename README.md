# np

Simple utility for creating new projects in Sbt

Steps it took to get this project started

    $ touch build.sbt
    $ mkdir -p src/{main,test}/scala
    $ e build.sbt # fill in the basics (name, organization, version)
    $ touch README.md && e README.md
    $ sbt
    # start coding

Desired steps to take to start this project

    $ sbt
    $ np org:me.lessis name:np version:0.1.0
    # start coding

New project? No problem.

Already have a project and want a sub project? No problem.

    $ sbt
    $ np name:my-sub-project dir:sub-project-dir

This will create a new sbt project source tree for a project named my-sub-project under
the directory named sub-project-dir relative you your projects base directory. From your main build configuration you can use this as a stub and reference it as.

    lazy val sub = Project("my-sub-project", file("sub-project-dir"))

Or remove the generated stub `build.sbt` and just use the generate source tree

### Settings

    np       # generates a new project given a set of options
    np:check # detects potential conflicts with generating a project, recommended before np
    np:usage # displays usage options

#### np option ref

`np` generates sbt projects given `key:value` options. Below is a list of current options

    org     Project organization. Defaults to sbt built-in default
    name    Project name. Defaults to sbt built-in default
    version Project version. Defaults to sbt built-in default
    plugin  Boolean indicator of whether the project is a plugin project. Defaults to false
    dir     Path to dir where np should generate project. Defaults to '.'

Doug Tangren (softprops) 2011
