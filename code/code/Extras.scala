package o1.adventure

/** class to define an object */
class Item(val name: String, val description: String):

  /** Returns a short textual representation of the item (its name, that is). */
  override def toString = this.name

end Item


/** Class to define a human */
class Human(val name: String, val description: String):

  /** Returns a short textual representation of the item (its name, that is). */
  override def toString = this.name

end Human

