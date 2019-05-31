
package root.scala.commands

import root.scala.files.Directory
import root.scala.system.State


class Rm(name: String) extends Command {

  /** Removes directories and files. Full path is supported
    * without "." and "..".
    */

  override def apply(state: State): State = {

    // 1. get working dir
    val wd = state.wd

    // 2. get absolute path
    val absolutePath = Command.getAbsolutePath(name, wd)

    // 3. do some checks
    if (Directory.ROOT_PATH.equals(absolutePath))
      state.setMessage("Nuclear war not supported yet!")
    else
      // 4. find the entry to remove and update entire structure
      doRm(state, absolutePath)
  }

  def doRm(state: State, path: String): State = {

    def rmHelper(currentDirectory: Directory, path: List[String]): Directory = {
      path match {
        case List(_) => currentDirectory.removeEntry(path.head)
        case _ =>
          val nextDirectory = currentDirectory.findEntry(path.head).get.asDirectory
          currentDirectory.replaceEntry(path.head, rmHelper(nextDirectory, path.tail))
      }
    }

    val tokens = Command.getTokens(path)

    if (Command.pathIsValid(state.root, tokens)) {
      val newRoot: Directory = rmHelper(state.root, tokens)
      State(newRoot, newRoot.findDescendant(state.wd.path.substring(1)))
    }
    else
      state.setMessage(path + ": no such file or directory")
  }

}
