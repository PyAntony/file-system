
package root.scala.commands

import root.scala.files.DirEntry
import root.scala.system.State


class Ls extends Command {

  /** Displays current directory entries.
    */

  override def apply(state: State): State = {
    val contents = state.wd.contents
    val niceOutput = createNiceOutput(contents)
    state.setMessage(niceOutput)
  }

  def createNiceOutput(contents: List[DirEntry]): String = {
    if (contents.isEmpty) ""
    else {
      val entry = contents.head
      entry.name + " [" + entry.getTYpe + "]\n" + createNiceOutput(contents.tail)
    }

  }

}
