
package root.scala.commands
import root.scala.system.State


class Pwd extends Command {

  /** Displays current working directory path.
    */

  override def apply(state: State): State =
    state.setMessage(state.wd.path)
}
