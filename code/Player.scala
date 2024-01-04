package o1.adventure

import scala.collection.mutable.Map
import scala.collection.mutable.Buffer
import scala.util.Random

/** class to define the player of the game. contains all possible actions and saves which elements are being carried by the character.*/
class Player(startingArea: Area):

  private var currentLocation = startingArea
  private var quitCommandGiven = false
  private var isCarrying = Buffer[String]()

  private val HIDING_PLACES = List("behind a column", "next to a wall", "under a table", "behind a door", "behind a big monster")

  /** Determines if the player has indicated a desire to quit the game. */
  def hasQuit = this.quitCommandGiven

  /** Returns the player’s current location. */
  def location = this.currentLocation

  /** Attempts to move the player in the given direction. This is successful if there
    * is an exit from the player’s current location towards the direction name. Returns
    * a description of the result: "You go DIRECTION." or "You can't go DIRECTION."
    *
    * Does not allow going east from the main hall if it is not 'chaotic'.*/
  def go(direction: String) =
    // only allows you to go from the main hall to Boo's door if there is chaos on the hall
    if this.currentLocation.name == "Main Hall" && direction == "east" then
      if !this.currentLocation.getStatus then
        s"You can only cross the Main Hall if you have created a distraction.\nUse a sock here or leave back to the hall entrance fast before anyone sees you."
      else
        val destination = this.location.neighbor(direction)
        this.currentLocation = destination.getOrElse(this.currentLocation)
        s"You sneak through the side of the hall towards Boo's door! The confusion is so big you make it unseen!"
    // any other case
    else
      val destination = this.location.neighbor(direction)
      this.currentLocation = destination.getOrElse(this.currentLocation)
      if destination.isDefined then "You go " + direction + "." else "You can't go " + direction + "."

  /** Signals that the player wants to quit the game. Returns a description of what happened within
  * the game as a result (which is the empty string, in this case). */
  def quit() =
    this.quitCommandGiven = true
    ""

  /** Adds item to the player's inventory */
  def get(element: String) =
    val success = this.currentLocation.remove(element)
    if !success then
      s"There is no $element here to pick up."
    else
      this.isCarrying.append(element)
      if element == "boo" then
        s"You pick Boo up."
      else
        s"You pick up the $element."


  /** Drop item if it's being carried and add it to the area */
  def drop(element: String) =
    if this.isCarrying.contains(element) then
      this.isCarrying = this.isCarrying.filter(_ != element)
      this.currentLocation.add(element)
      s"You dropped $element"
    else
      s"You cannot drop something you are not carrying."


  /** prints out the players inventory */
  def inventory() =
    if this.isCarrying.isEmpty then
      "You are empty-handed."
    else
      var to_return = "You are carrying:"
      for item <- this.isCarrying do
        to_return = to_return + "\n" + item
      to_return

  /** action that allows user to transform a location's status to chaotic. this created a distraction on the player's current location. */
  def use(element: String) =
    if element == "sock" then
      // remove sock from inventory but not add it to the current location since the sock can only be used once
      this.isCarrying = this.isCarrying.filter(_ != "sock")
      //create chaos on the location the sock is dropped
      this.currentLocation.createChaos()
      if this.currentLocation.name == "Main Hall" then
        s"You throw the sock into the Main Hall and chaos begin!\nIt's easy to distract monsters sometimes...\nQuick! Use the mass histeria to get by unseen and reach Boo's door."
      else
        s"You throw the sock onto the floor and some chaos starts to form.\nBetter get out before anyone finds out it was you!\nSullivan: 'I thought the sock would have had a better use on the Main Hall, but I'll trust your strategy!\n          We'll have to take the long round to the door now.'"
    else
      s"You cannot use $element! That would get you caught! Try using a human sock instead."


  /** returns a textual description of a randomized hiding location. */
  def hide() =
    val i = Random.shuffle(this.HIDING_PLACES).head
    s"You manage to hide $i!"


  /** returns a textual description of all possible in-game commands. */
  def help() =
    var info = "Here are all your command options:"
    info += "\n- go + direction (i.e. go south, go north, go east or go west)"
    info += "\n- get + element (i.e. get sock, get Boo)"
    info += "\n- drop + element (i.e. drop sock, drop boo)"
    info += "\n- use sock (to use the sock to distract other monsters. Note: the sock can only be used once.)"
    info += "\n- inventory (to get a list of things you are carrying)"
    info += "\n- hide"
    info += "\n- help (to get list of commands)"
    info += "\n- quit (to quit the program)"
    info


  /** Returns a brief description of the player’s state, for debugging purposes. */
  override def toString = "Now at: " + this.location.name

end Player

