
package root.scala.commands

import root.scala.files.{DirEntry, Directory}
import root.scala.system.State


class Cd(dir: String) extends Command {

  /** Navigates the file system (from root or current directory).
    */

  override def apply(state: State): State = {

    val root = state.root
    val wd = state.wd

    // 1. find the absolute path of the directory I want to cd to
    val absolutePath = Command.getAbsolutePath(dir, wd)

    // 2. find the directory to cd to given the path
    val destinationDirectory = doFindEntry(root, absolutePath)

    // 3. change the state given the new directory
    destinationDirectory match {
      case None => state.setMessage(dir + ": No such directory")
      case Some(d) => State(root, d.asDirectory)
    }
  }

  def doFindEntry(root: Directory, path: String): Option[DirEntry] = {

    def collapseInList(tokens: List[String]): List[String] = {
      /** To collapse all ["..", "."] in list of tokens
       */
      val newTokens = tokens.filterNot(_.equals("."))
      val index = newTokens.indexOf("..")
      val start = if (index != 0) index - 1 else index

      if (index == -1) newTokens
      else collapseInList(newTokens.take(start) ++ newTokens.drop(index + 1))
    }

    def navigateSystem(currentDir: Directory, pathList: List[String]): Option[DirEntry] = {
      pathList match {
        case Nil => Some(currentDir)
        case List(".", _*) => navigateSystem(currentDir, pathList.tail)
        case _ =>
          val nextDir = currentDir.findEntry(pathList.head)
          nextDir match {
            case None => None
            case Some(f) if !f.isDirectory => None
            case Some(d) => navigateSystem(d.asDirectory, pathList.tail)
          }
      }
    }

    val tokens = Command.getTokens(path)
    if (tokens.head.equals("..")) None
    else navigateSystem(root, collapseInList(tokens))
  }

}
