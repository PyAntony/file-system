
package root.scala.commands

import root.scala.files.{DirEntry, Directory}
import root.scala.system.State


class Cp(args: List[String]) extends Command {

  /** Copy File/Directory to path. Full path is supported without "." or "..".
    * Example: cp dir1/dir2/file.txt dir3/newfile.txt
   */

  override def apply(state: State): State = {
    args match {

      case Nil => state.setMessage("missing file operand")
      case List(token) => state.setMessage(s"missing destination file operand after $token")
      case List(token1, token2) => doCp(state, token1, token2)
      case _ => state.setMessage("too many arguments")
    }
  }

  def getEntry(currentDir: Directory, path: List[String]): Option[DirEntry] = {
    path match {
      case List(_) => currentDir.findEntry(path.head)
      case _ => getEntry(currentDir.findEntry(path.head).get.asDirectory, path.tail)
    }
  }

  def copyEntry(currentDir: Directory, tokens: List[String], entry: DirEntry): Directory = {
    tokens match {
      case List(_) => currentDir.addEntry(entry.updateName(tokens.head))
      case _ =>
        val nextDir = currentDir.findEntry(tokens.head)
        currentDir.replaceEntry(tokens.head, copyEntry(nextDir.get.asDirectory, tokens.tail, entry))
    }
  }

  def noSuchEntry(prefix: String, state: State): State =
    state.setMessage(prefix + ": no such file or directory")

  def doCp(state: State, source: String, destination: String): State = {

    // 1. get paths as tokens
    val wd = state.wd
    val root = state.root

    val pathSource = Command.getAbsolutePath(source, wd)
    val tokensSource = Command.getTokens(pathSource)

    val pathDestination = Command.getAbsolutePath(destination, wd)
    val tokensDestination = Command.getTokens(pathDestination)

    // 2. validate source and destination paths
    val sourceIsValid = Command.pathIsValid(root, tokensSource)
    val destinationIsValid =
      if (tokensDestination.size == 1) true
      else Command.pathIsValid(root, tokensDestination.init)

    if (!sourceIsValid) {
      return noSuchEntry(pathSource, state)
    }
    if (!destinationIsValid) {
      return noSuchEntry(pathDestination, state)
    }
    // 3. get the entry to copy
    val entry = getEntry(root, tokensSource)

    // 4. get new root with copied entry
    val newRoot = copyEntry(root, tokensDestination, entry.get)

    State(newRoot, newRoot.findDescendant(state.wd.getAllFoldersInPath))
  }

}
