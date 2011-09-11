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

### Installation

In most cases a global installation will make the most sense as the target usage for this plugin is the creation of new projects

If you have a `~/.sbt` directory created, in a `~/.sbt/plugins/project/build.scala` file add the following

    import sbt._

    object Plugins extends Build {
      lazy val root = Project("root", file(".")) dependsOn(
        uri("git://github.com/softprops/np.git#master")
      )
    }

This will make `np`'s setting available to all of your sbt projects.

If you have a lot of projects that use the same ivy organization id or you always start projects with the same version conventions, you may want to define your own custom global overrides.

To do so, in a `~/.sbt/np.sbt` file, add the following.

    (np.Keys.defaults in Np) <<= (np.Keys.defaults in Np)(d =>
      d.copy(org = "me.lessis", version = "0.1.0-SNAPSHOT")
    )

See the `np` option reference section below for all available options

### Settings

    np          # generates a new project given a set of options
    np:check    # detects potential conflicts with generating a project, recommended before np
    np:usage    # displays usage options
    np:defaults # default values for options

#### np option reference

`np` generates sbt projects given `key:value` options. Below is a list of current options

    org     Project organization. Defaults to defaults key sbt built-in default
    name    Project name. Defaults to defaults key or sbt built-in default
    version Project version. Defaults to defaults key or sbt built-in default
    plugin  Boolean indicator of whether the project is a plugin project. Defaults to defaults key or false
    dir     Path to dir where np should generate project. Defaults to defaults key or '.'

Doug Tangren (softprops) 2011
