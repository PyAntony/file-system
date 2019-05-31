
package root.scala.commands

import root.scala.system.State


class UnknownCommand extends Command {

  override def apply(state: State): State =
    state.setMessage("Command not found!")

}
