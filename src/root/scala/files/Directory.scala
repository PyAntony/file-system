
package root.scala.files

import root.scala.system.FilesystemException


class Directory(override val parentPath: String,
                override val name: String,
                val contents: List[DirEntry]) extends DirEntry(parentPath, name) {

  def hasEntry(name: String): Boolean = findEntry(name).nonEmpty

  def getAllFoldersInPath: List[String] =
    path.substring(1).split(Directory.SEPARATOR).toList.filterNot(_.isEmpty)

  def findDescendant(path: List[String]): Directory =
    if (path.isEmpty) this
    else findEntry(path.head).get.asDirectory.findDescendant(path.tail)

  def findDescendant(relativePath: String): Directory =
    if (relativePath.isEmpty) this
    else findDescendant(relativePath.split(Directory.SEPARATOR).toList)

  def removeEntry(entryName: String): Directory =
    if (!hasEntry(entryName)) this
    else new Directory(parentPath, name, contents.filterNot(_.name.equals(entryName)))

  def addEntry(newEntry: DirEntry): Directory =
    new Directory(parentPath, name, contents :+ newEntry)

  def findEntry(entryName: String): Option[DirEntry] = {
    val entryList = contents.filter(_.name.equals(entryName))
    entryList.headOption
  }

  def isRoot: Boolean = parentPath.isEmpty

  def isDirectory: Boolean = true
  def isFile: Boolean = false

  def replaceEntry(entryName: String, newEntry: DirEntry): Directory =
    new Directory(parentPath, name, contents.filterNot(_.name.equals(entryName)) :+ newEntry)

  def asDirectory: Directory = this
  def asFile = throw new FilesystemException("A directory cannot be converted to a file!")

  def getTYpe: String = "Directory"

  def updateName(newName: String): DirEntry =
    new Directory(parentPath, newName, contents)
}


object Directory {
  val SEPARATOR = "/"
  val ROOT_PATH = "/"

  def ROOT: Directory = Directory.empty("", "")
  def empty(parentPath: String, name: String): Directory =
    new Directory(parentPath, name, List())
}
