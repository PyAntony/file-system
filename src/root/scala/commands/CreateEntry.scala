
package root.scala.commands

import root.scala.files.{DirEntry, Directory}
import root.scala.system.State


abstract class CreateEntry(name: String) extends Command {

  /** Creates entry in current directory only.
    */

  override def apply(state: State): State = {

    val wd = state.wd

    if (wd.hasEntry(name)) {
      println(wd.contents)
      state.setMessage("Entry " + name + " already exists!")
    }
    else if (name.contains(Directory.SEPARATOR)) {
      state.setMessage(name + " must not contain separators!")
    }
    else if (checkIllegal(name)) {
      state.setMessage(name + ": illegal entry name!")
    }
    else doCreateEntry(state, name)
  }

  def checkIllegal(name: String): Boolean = name.contains(".")

  def doCreateEntry(state: State, name: String): State = {

    def updateStructure(currentDirectory: Directory,
                        path: List[String],
                        newEntry: DirEntry): Directory = {

      if (path.isEmpty) currentDirectory.addEntry(newEntry)
      else {
        val oldEntry = currentDirectory.findEntry(path.head).get.asDirectory
        currentDirectory.replaceEntry(oldEntry.name, updateStructure(oldEntry, path.tail, newEntry))
      }
    }

    val wd = state.wd

    // 1. get all directory names in this path
    val allDirsInPath: List[String] = wd.getAllFoldersInPath

    // 2. create the dirEntry
    val newEntry: DirEntry = createSpecificEntry(state)

    // 3. update the entire structure starting from the root (directory is IMMUTABLE)
    val newRoot = updateStructure(state.root, allDirsInPath, newEntry)

    // 4. find new working dir INSTANCE in the NEW dir structure
    val newWd = newRoot.findDescendant(allDirsInPath)

    State(newRoot, newWd)
  }

  def createSpecificEntry(state: State): DirEntry

}
