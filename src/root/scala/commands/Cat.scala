package root.scala.commands
import root.scala.system.State

class Cat(filename: String) extends Command {

  /** Displays contents in File objects.
    */

  override def apply(state: State): State = {
    val wd = state.wd
    val dirEntry = wd.findEntry(filename)

    dirEntry match {
      case None => state.setMessage(filename + ": no such file")
      case Some(f) if !f.isFile => state.setMessage(filename + ": no such file")
      case Some(d) => state.setMessage(d.asFile.contents)
    }
  }

}
