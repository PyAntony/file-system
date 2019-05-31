
package root.scala.system

import root.scala.files.Directory


class State(val root: Directory, val wd: Directory, val output: String) {
  /** Holds the root directory (which holds the entire file structure),
    * the working directory, and the displayed message.
    */

  def show(): Unit = {
    println(output)
    print(State.SHELL_TOKEN)
  }

  def setMessage(message: String): State =
    State(root, wd, message)
}


object State {
  val SHELL_TOKEN = "$ "

  def apply(root: Directory, wd: Directory, output: String = ""): State =
    new State(root, wd, output)
}
