
package root.scala.commands

import root.scala.files.{DirEntry, Directory}
import root.scala.system.State


trait Command extends (State => State)


object Command {

  /** COMMANDS */

  val MKDIR = "mkdir"
  val LS = "ls"
  val PWD = "pwd"
  val TOUCH = "touch"
  val CD = "cd"
  val RM = "rm"
  val ECHO = "echo"
  val CAT = "cat"
  val CP = "cp"


  /********** HELPER METHODS **********/

  def emptyCommand: Command = (state: State) => state

  def incompleteCommand(name: String): Command =
    (state: State) => state.setMessage(name + ": incomplete command!")

  def getAbsolutePath(entry: String, workingDir: Directory): String ={
    /** Gets entry's absolute path from root
      */
    if (entry.startsWith(Directory.SEPARATOR)) entry
    else if (workingDir.isRoot) workingDir.path + entry
    else workingDir.path + Directory.SEPARATOR + entry
  }

  def getTokens(path: String): List[String] =
    path.substring(1).split(Directory.SEPARATOR).toList

  def getAbsoluteAsTokens(entry: String, workingDir: Directory): List[String] =
    getTokens(getAbsolutePath(entry, workingDir))

  def allEntriesAreValid(currentDir: Directory, tokens: List[String]): Boolean = {
    /** Validates all dirEntries in path from root
      */
    if (tokens.size == 1)
      return currentDir.hasEntry(tokens.head)

    val nextDir = currentDir.findEntry(tokens.head)
    if (nextDir.getOrElse(return false).isDirectory)
      allEntriesAreValid(nextDir.get.asDirectory, tokens.tail)
    else
      false
  }

  def pathIsValid(root: Directory, tokens: List[String]): Boolean =
    tokens.nonEmpty && allEntriesAreValid(root, tokens)


  /********** MAIN INPUT ENTRY METHOD **********/

  def from(input: String): Command = {
    val tokens: List[String] = input.split(" ").toList
    val cmd = if (tokens.nonEmpty) tokens.head else "no command"
    val incomplete = tokens.size < 2

    cmd match {
      case "no command" => emptyCommand
      case MKDIR => if (incomplete) incompleteCommand(MKDIR) else new Mkdir(tokens(1))
      case TOUCH => if (incomplete) incompleteCommand(TOUCH) else new Touch(tokens(1))
      case CD => if (incomplete) incompleteCommand(CD) else new Cd(tokens(1))
      case RM => if (incomplete) incompleteCommand(RM) else new Rm(tokens(1))
      case CAT => if (incomplete) incompleteCommand(CAT) else new Cat(tokens(1))
      case ECHO => new Echo(tokens.tail)
      case PWD => new Pwd
      case LS => new Ls
      case CP => new Cp(tokens.tail)
      case _ => new UnknownCommand
    }
  }

}
