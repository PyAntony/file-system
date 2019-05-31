
package root.scala.commands

import root.scala.files.{Directory, File}
import root.scala.system.State


class Echo(args: List[String]) extends Command {

  /** Appends (">>") or Sets (">") text to File object in current directory only.
    * If no operator is present the text is displayed instead.
    * Example: "echo some text goes here >> filename"
    * Example: "echo some text here is displayed"
    */

  override def apply(state: State): State = {

    if (args.isEmpty) state
    else if (args.length == 1) state.setMessage(args.head)
    else {
      val operator = args.init.last
      val filename = args.last
      val contents = args.init.init.mkString(" ") //dropping last 2 items

      if (operator.equals(">>"))
        doEcho(state, contents, filename, append=true)
      else if (operator.equals(">"))
        doEcho(state, contents, filename, append=false)
      else
        state.setMessage(args.mkString(" "))
    }
  }

  def doEcho(state: State, contents: String, filename: String, append: Boolean): State = {

    def doEchoHelper(currentDir: Directory)
                    (tokens: List[String])
                    (text: String)
                    (append: Boolean): Directory = {
      tokens match {
        case List(file) =>
          val newFile = currentDir.findEntry(tokens.head).get.asFile.setOrAppend(append, text)
          currentDir.replaceEntry(file, newFile)
        case _ =>
          val nextDir = currentDir.findEntry(tokens.head).get.asDirectory
          currentDir.replaceEntry(tokens.head, doEchoHelper(nextDir)(tokens.tail)(text)(append))
      }
    }

    if (filename.contains(Directory.SEPARATOR))
      state.setMessage("Echo: filename must not contain separators!")

    else if (!state.wd.hasEntry(filename))
      state.setMessage(filename + ": no such file")

    else {
      val doEchoHelp = doEchoHelper(_: Directory)(_: List[String])(contents)(append)
      val newRoot: Directory =
        doEchoHelp(state.root, state.wd.getAllFoldersInPath :+ filename)

      State(newRoot, newRoot.findDescendant(state.wd.getAllFoldersInPath))
    }
  }

}
