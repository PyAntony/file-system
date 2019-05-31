
package root.scala.commands

import root.scala.files.{File, DirEntry}
import root.scala.system.State


class Touch(name: String) extends CreateEntry(name) {

  override def createSpecificEntry(state: State): DirEntry =
    File.empty(state.wd.path, name)

}
