package bwsw.testcase

/** @param name - name of this Subject
  * @param lowPriority - indicates that this Subject avoids of owning any of Objects
  * @param objects - Set of Objects this Subject can own
  */
case class Subject(name: String, lowPriority: Boolean, objects: Set[Object])

