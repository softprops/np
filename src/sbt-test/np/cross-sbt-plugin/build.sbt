seq(npSettings:_*)

InputKey[Unit]("contents") <<= inputTask { (argsTask: TaskKey[Seq[String]]) =>
  (argsTask, streams) map {
    (args, out) =>
      args match {
        case Seq(given, expected) =>
          if(IO.readLines(file(given)).equals(IO.readLines(file(expected)))) out.log.debug(
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
