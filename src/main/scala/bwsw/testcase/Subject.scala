package bwsw.testcase

/** @param name - name of this Subject
  * @param lowPriority - indicates if this Subject avoids owning of any of Objects
  * @param objects - Set of Objects this Subject can own
  */
case class Subject(name: String, lowPriority: Boolean, objects: Set[Object])

