
package root.scala.files


abstract class DirEntry(val parentPath: String, val name: String) {

  def path: String = {
    val separator = if (parentPath.equals(Directory.SEPARATOR)) "" else "/"
    parentPath + separator + name
  }

  def updateName(newName: String): DirEntry

  def asDirectory: Directory
  def asFile: File

  def isDirectory: Boolean
  def isFile: Boolean

  def getTYpe: String
}
