import sbt._
import Project.Initialize

object Plugin extends sbt.Plugin {
  import sbt.Keys._

  object np {

    import java.io.File
    import java.lang.Boolean.{parseBoolean => bool}

    private [np] object BuildSbt {
      private val SbtVersion = "{sbtVersion}"

      /** Handles cross sbt versioning.  */
      private def versionBind(version: String, plugin: Boolean) = {
        if(!plugin) """:= "%s"""".format(version)
        else {
          if(version.contains(SbtVersion)) {
            """<<= sbtVersion(v => "%s".format(v))""".format(version.replace(SbtVersion, "%s"))
          } else """:= "%s"""".format(version)
        }
      }

      def apply(plugin: Boolean, org: String, name: String, version: String) =
        """%sorganization := "%s"
        |
        |name := "%s"
        |
        |version %s""".stripMargin.format(
          if(plugin) "sbtPlugin := true\n\n" else "",
          org,
          name,
          versionBind(version, plugin)
        )
    }

    private [np] object Usage {
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

    case class Defaults(org: String, name: String, version: String,
                    plugin: Boolean = false, dir: String = ".")

    object Keys {
      val np = InputKey[Unit]("np", "Sbt project generator")
      val defaults = SettingKey[Defaults]("defaults", "Default options used to generate projects")
      val check = InputKey[Unit]("check", "Does a dry run to check for conflicts")
      val usage = TaskKey[Unit]("usage", "Displays np usage info")
    }
    import np.Keys._

    val Config = config("np")

    private def extract(args: Seq[String], pbase: File, defaults: Defaults) = {

      val Name  = """name\:(\S+)""".r
      val Vers  = """version\:(\S+)""".r
      val Org   = """org\:(\S+)""".r
      val Plgin = """plugin\:(\S+)""".r
      val Dir   = """dir\:(\S+)""".r

      def first[T](default: T)(pf: PartialFunction[String, T]) =
        args.collect(pf).headOption.getOrElse(default)

      val (p, o, n, v, d) = (
        first(false) { case Plgin(p) => bool(p) },
        first(defaults.org)   { case Org(o)   => o },
        first(defaults.name)  { case Name(n)  => n },
        first(defaults.version) { case Vers(v)  => v },
        first(defaults.dir)   { case Dir(d) => d }
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

    // will NOT auto-mix into project settings
    def settings: Seq[Setting[_]] = inConfig(Config)(Seq(
      defaults <<= (name, organization, version)(Defaults(_, _, _)),
      usage <<= usageTask,
      check <<= inputTask { (argsTask: TaskKey[Seq[String]]) =>
        (argsTask, baseDirectory, streams, defaults in Config) map {
          (args, bd, out, defaults) =>
            val (_, base) = extract(args, bd, defaults)
            val (bf, dirs) = (new File(base, "build.sbt"), genDirs(base))
            dirs :+ bf filter(_.exists) match {
              case Nil =>
                out.log.info("Looks good. Run `np` task to generate project.")
              case existing =>
                out.log.warn("The following files/directories already exist\n\n%s".format(
                  existing.mkString("\n\n"))
                )
            }
        }
      }
    )) ++ Seq(
      Keys.np <<= inputTask { (argsTask: TaskKey[Seq[String]]) =>
        (argsTask, baseDirectory, streams, defaults in Config) map {
          (args, bd, out, defaults) =>
            val (scpt, base) = extract(args, bd, defaults)
            val (bf, dirs) = (new File(base, "build.sbt"), genDirs(base))

            // error out if any of the target files to generate
            // already exist
            if((dirs :+ bf).find(_.exists).isDefined) error(
              "\nexisting project detected at the path %s" format bf.getParent
            )

            IO.write(bf, scpt)
            out.log.info("Generated build file")

            IO.createDirectories(dirs)
            out.log.info("Generated source directories")
        }
     })
   }
}
