package np

import sbt._
import Project.Initialize

object Keys {
  val np = InputKey[Unit]("np", "Sbt project generator")
  val check = InputKey[Unit]("check", "Does a dry run to check for conflicts")
  val usage = TaskKey[Unit]("usage", "Displays np usage info")
}

object BuildSbt {
  def apply(plugin: Boolean, org: String, name: String, version: String) =
    """%sorganization := "%s"
    |
    |name := "%s"
    |
    |version := "%s"""".stripMargin.format(
      if(plugin) "sbtPlugin := true\n\n" else "",
      org,
      name,
      version
    )
}

object Usage {
  val display = """
  |Usage: <key>:<value> <key2>:<value2> ...
  |
  |all key arguments have default values
  |
  |Keys
  |  org     Project organization. Defaults to sbt built-in default
  |  name    Project name. Defaults to sbt built-in default
  |  version Project version. Defaults to sbt built-in default
  |  plugin  Boolean indicator of whether the project is a plugin project. Defaults to false
  |  dir     Path to dir where np should generate project. Defaults to '.'
  |""".stripMargin
}

object Plugin extends sbt.Plugin {
  import sbt.Keys._
  import np.Keys._
  import java.io.File
  import java.lang.Boolean.{parseBoolean => bool}

  val Np = config("np") extend(Runtime)

  private def extract(args: Seq[String], name: String, org: String,
                      rev: String, pbase: File, dir: String = ".") = {

    val Name  = """name\:(\S+)""".r
    val Vers  = """version\:(\S+)""".r
    val Org   = """org\:(\S+)""".r
    val Plgin = """plugin\:(\S+)""".r
    val Dir   = """dir\:(\S+)""".r

    def first[T](default: T)(pf: PartialFunction[String, T]) =
      args.collect(pf).headOption.getOrElse(default)

    val (p, o, n, v, d) = (
      first(false) { case Plgin(p) => bool(p) },
      first(org)   { case Org(o)   => o },
      first(name)  { case Name(n)  => n },
      first(rev)   { case Vers(v)  => v },
      first(dir)   { case Dir(d) => d }
    )

    (BuildSbt(p, o, n, v), d match {
      case "." => pbase
      case path => new File(pbase, path)
    })
  }

  private def configDirs(conf: String)(base: File) =
    Seq("scala", "resources") map { d => new File(base, "src/%s/%s".format(conf,d)) }

  private def mainDirs = configDirs("main")_

  private def testDirs = configDirs("test")_

  private def genDirs(base: File): Seq[File] = mainDirs(base) ++ testDirs(base)

  private def usageTask: Initialize[Task[Unit]] =
    (streams) map {
      (out) =>
        out.log.info(Usage.display)
    }

  // will auto mix into project settings
  override def settings: Seq[Setting[_]] = inConfig(Np)(Seq(
    usage <<= usageTask,
    check <<= inputTask { (argsTask: TaskKey[Seq[String]]) =>
      (argsTask, baseDirectory, streams, name, organization, version) map {
        (args, bd, out, name, org, rev) =>
          val (_, base) = extract(args, name, org, rev, bd, ".")
          val (bf, dirs) = (new File(base, "build.sbt"), genDirs(base))
          dirs :+ bf filter(_.exists) match {
            case Nil => ()
            case existing =>
              out.log.warn("The following files/directories already exist\n\n%s" format(existing.mkString("\n\n")))
          }
      }
    }
  )) ++ Seq(
    np <<= inputTask { (argsTask: TaskKey[Seq[String]]) =>
      (argsTask, baseDirectory, streams, name, organization, version) map {
        (args, bd, out, name, org, vers) =>
          val (scpt, base) = extract(args, name, org, vers, bd, ".")
          val (bf, dirs) = (new File(base, "build.sbt"), genDirs(base))

          // error out if any of the target files to generate
          // already exist
          if((dirs :+ bf).find(_.exists).isDefined) error(
            "existing project detected at this path"
          )

          IO.write(bf, scpt)
          out.log.info("Generated build file")

          IO.createDirectories(dirs)
          out.log.info("Generated source directories")
       }
    })
}
