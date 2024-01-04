package o1.adventure

import scala.collection.mutable.Map
import scala.collection.mutable.Buffer

/** The class `Area` represents locations in a text adventure game world. A game world
  * consists of areas. An area also has a name and a description. */
class Area(var name: String, var description: String):

  private val neighbors = Map[String, Area]()
  private var contains = Buffer[String]()
  
  private var chaos = false

  /** Returns the area that can be reached from this area by moving in the given direction. The result
  * is returned in an Option; None is returned if there is no exit in the given direction. */
  def neighbor(direction: String) = this.neighbors.get(direction)

  /** Adds an exit from this area to the given area. The neighboring area is reached by moving in
  * the specified direction from this area. */
  def setNeighbor(direction: String, neighbor: Area) =
    this.neighbors += direction -> neighbor

  /** Adds exits from this area to the given areas. Calling this method is equivalent to calling
  * the setNeighbor method on each of the given direction–area pairs. */
  def setNeighbors(exits: Vector[(String, Area)]) =
    this.neighbors ++= exits

  /** returns elements in the area */
  def has() = this.contains

  /** Returns a description of the place as well as any extra elemenets present and the possible
    * exit directions. */
  def fullDescription =
    var contentsList = ""
    if this.contains.nonEmpty then
      contentsList = "\nYou see here: "
      for i <- this.contains do
        contentsList += i + " "
    val exitList = "\n\nExits available: " + this.neighbors.keys.mkString(" ")
    this.description + contentsList + exitList

  /** Add extra element on to the area */
  def add(element: String) =
    this.contains.append(element)

  /** Remove extra element from the area */
  def remove(element: String): Boolean =
    if this.contains.contains(element) then
      this.contains = this.contains.filter (_ != element)
      true
    else
      false
      
  /** change the location's 'chaos status' to true. */
  def createChaos() = this.chaos = true
  
  /** returns true if the area is on chaos and false if not. */
  def getStatus = this.chaos

  /** Returns a single-line description of the area for debugging purposes. */
  override def toString = this.name + ": " + this.description.replaceAll("\n", " ").take(150)

end Area

