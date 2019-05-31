
package root.scala.commands

import root.scala.files.{DirEntry, Directory}
import root.scala.system.State


class Mkdir(name: String) extends CreateEntry(name) {

  override def createSpecificEntry(state: State): DirEntry =
    Directory.empty(state.wd.path, name)
}
