
package root.scala.files

import root.scala.system.FilesystemException


class File(override val parentPath: String,
           override val name: String,
           val contents: String) extends DirEntry(parentPath, name) {

  def asFile: File = this
  def asDirectory: Directory =
    throw new FilesystemException("A file cannot be converted to a directory!")

  def isDirectory: Boolean = false
  def isFile: Boolean = true

  def getTYpe: String = "File"

  def setContents(newContents: String): File =
    new File(parentPath, name, newContents)

  def appendContents(newContents: String): File =
    setContents(contents + "\n" + newContents)

  def updateName(newName: String): DirEntry =
    new File(parentPath, newName, contents)

  def setOrAppend(append: Boolean, newContents: String): File =
    if (append)
      appendContents(newContents)
    else
      setContents(newContents)
}


object File {

  def empty(parentPath: String, name: String): File =
    new File(parentPath, name, "")
}
