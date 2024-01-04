package o1.adventure

/** Class to execute actions a player could take. */
class Action(input: String):

  private val commandText = input.trim.toLowerCase
  private val verb        = commandText.takeWhile( _ != ' ' )
  private val modifiers   = commandText.drop(verb.length).trim

  /** function that executes the action. each are called by a case match option */
  def execute(actor: Player) =
    this.verb match
      case "go"        => Some(actor.go(this.modifiers))
      case "get"       => Some(actor.get(this.modifiers))
      case "drop"      => Some(actor.drop(this.modifiers))
      case "use"       => Some(actor.use(this.modifiers))
      case "inventory" => Some(actor.inventory())
      case "hide"      => Some(actor.hide())
      case "help"      => Some(actor.help())
      case "quit"      => Some(actor.quit())
      case other       => None

  /** Returns a textual description of the action object, for debugging purposes. */
  override def toString = s"$verb (modifiers: $modifiers)"

end Action

