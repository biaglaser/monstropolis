package o1.adventure

/** Describes the universe in which the gameis played. This class initializes the map, player and
  * extra elements. It also controls whether on not the game is ongoing or stopped. This class also
  * displays the welcoming and goodbye messages. */
class Adventure:

  /** the name of the game */
  val title = "Monsters Inc Challenge"

  /** The number of current turns and max number of turns before getting found by Randall. */
  var turnCount = 0
  val timeLimit = 20


  /** Initialize all areas */
  private val middle =       Area("Corridor", "You are in the building's main corridor.\nNo matter how long you've been working here, it is hard to get used to its length!")
  private val outside =      Area("Outside", "You have exited the building and is on the outside terrace.\nSullivan: 'We can enjoy the sun later, better go back inside now, we are on a mission!'")
  private val lockerRoom =   Area("Locker Room", "You are in the locker room.\nWhy does it smell so bad in here?")
  private val bathroom =     Area("Bathroom", "You are in the bathroom.")
  private val offices =      Area("Offices", "You are in the office area.\nThere are so many cubicles and meeting rooms here. Careful not to get lost!")
  private val hallEntrance = Area("Hall Entrance", "You are in front of the Main Hall, and you see Boo's door across the room.\nSullivan: 'The Main Hall (east) seem to be busy... we might be able to sneak through the crowd if we have a sock to distract everyone!\n          Otherwise we might have to take the long route (south) past the boring receptionist...'")
  private val mainHall =     Area("Main Hall", "You are in the main hall.\nYou should use the sock for a distraction or go back to the entrance before you get spotted!")
  private val reception =    Area("Reception", "You are passing by the receptionist.\nSullivan: 'Damn it! Roz saw us... now we will lose 4 turns trapped talking to her. Let's get out fast before we lose any more rounds.'")
  private val vent =         Area("Vent", "You are squeezing through the vents. This should lead you straight to Boo's door!")
  private val door =         Area("Door", "You made it to Boo's door! Now let's drop her here to complete the challange!")

  private val destination = door


  /** set map by adding neighbors to each area */
  middle      .setNeighbors(Vector("north" -> outside,      "east" -> hallEntrance, "south" -> offices,   "west" -> bathroom    ))
  outside     .setNeighbors(Vector(                                                 "south" -> middle,    "west" -> lockerRoom  ))
  lockerRoom  .setNeighbors(Vector(                         "east" -> outside,      "south" -> bathroom                         ))
  bathroom    .setNeighbors(Vector("north" -> lockerRoom,   "east" -> middle,       "south" -> offices                          ))
  offices     .setNeighbors(Vector("north" -> middle,                               "south" -> offices,   "west" -> bathroom    ))
  hallEntrance.setNeighbors(Vector(                         "east" -> mainHall,     "south" -> reception, "west" -> middle      ))
  mainHall    .setNeighbors(Vector(                         "east" -> door,                               "west" -> hallEntrance))
  reception   .setNeighbors(Vector(                         "east" -> vent,                               "west" -> hallEntrance))
  vent        .setNeighbors(Vector("north" -> door,                                                       "west" -> reception   ))
  door        .setNeighbors(Vector(                                                 "south" -> vent,      "west" -> mainHall    ))

  /** initialize player */
  val player = Player(middle)

  /** Create extra features and add to locations (sock and boo) */
  val sock = Item("sock", "It's a human stinky sock. The other monsters would be terrified!")
  val human = Human("boo", "She's so small and cute! Can't believe we were once scared of her...")

  /** add extra elements to their default locations */
  lockerRoom.add(sock.name)
  bathroom.add(human.name)

  /** introduce game and rules to user */
  def introduction = "Welcome to Monstropolis!\n\nYour task is to play as Mike Wazowski. You need to help Sullivan find Boo and take her to her Door safely!\nBe careful though, if you cannot complete your task within 20 rounds, Randall will find her first and you'll get fired!\n\nHints:\n- Collecting a sock might come in handy when crossing the Main Hall.\n- The possible commands are listed by entering 'help'.\n- Everytime you enter a command it is counted as a turn, even if the command is wrong (with the exception of the 'help' command).\n\nGood luck and remember to try to avoid the receptionist!"

  /** goodbye message to indicate the game has ended */
  def goodbye =
    var info = ""
    if this.isComplete then
      info = "You successfully brought Boo back to her door safely and on time!!"
    else if this.turnCount == this.timeLimit then
      info = "You ran out of turns!\nRandall found Boo and reported you to the boss... Better start looking for new jobs."
    else
      info = "Better luck next time..."
    info + "\n\nThank you for playing! You're welcomed back in Monstropolis anytime."

  /** determines if the game is complete (if the player has won) */
  def isComplete: Boolean =
    var complete = false
    if this.player.location == this.destination then
      if this.destination.has().contains("boo") then
        complete = true
    complete

  /** Determines whether the player has won, lost, or quit, thereby ending the game. */
  def isOver = this.isComplete || this.player.hasQuit || this.turnCount == this.timeLimit

  /** Plays a turn by executing the given in-game command. Increase the game's turn count according to the rules.
    * Returns a textual report of what happened, or an error message if the command was unknown. */
  def playTurn(command: String): String =
    val action = Action(command)
    val outcomeReport = action.execute(this.player)

    // 4 turns lost if at reception
    if this.player.location == reception && command != "help" then
      this.turnCount += 4
    else if command != "help" then
      this.turnCount += 1

    val to_return = outcomeReport.getOrElse(s"Unknown command: \"$command\".")
    to_return

end Adventure

