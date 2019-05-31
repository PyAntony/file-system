
package root.scala.system

import java.util.Scanner
import root.scala.commands.Command
import root.scala.files.Directory


object Filesystem extends App {

  // Create root (empty directory)
  val root = Directory.ROOT
  // Create initial state.
  val state = State(root, root)
  // To read in user input
  val scanner = new Scanner(System.in)

  def systemIteration(state: State = state, scanner: Scanner = scanner): Unit = {

    state.show()
    systemIteration(Command.from(scanner.nextLine()).apply(state))
  }

  systemIteration()
}
