seq(npSettings:_*)

(NpKeys.defaults in (Compile, NpKeys.np)) <<= (NpKeys.defaults in (Compile,NpKeys.np))(d =>
  d.copy(name = "foo", org = "com.bar",
         dir = "foo", version="0.1-TEST")
)

InputKey[Unit]("contents") <<= inputTask { (argsTask: TaskKey[Seq[String]]) =>
  (argsTask, streams) map {
    (args, out) =>
      args match {
        case Seq(given, expected) =>
          if(IO.read(file(given)).trim.equals(IO.read(file(expected)).trim)) out.log.debug(
            "Contents match"
          )
          else error(
            "Contents of (%s)\n%s does not match (%s)\n%s" format(
              given, IO.read(file(given)), expected, IO.read(file(expected))
            )
          )
      }
  }
}
