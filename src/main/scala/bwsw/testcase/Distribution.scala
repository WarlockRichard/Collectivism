package bwsw.testcase

/** Represent distribution of Objects between Subjects
  *
  * bwsw.testcase.OwnershipDistributor can be used to add or remove
  * subjects according to distributive justice
  * @param objects an objects that can be distributed between subjects
  * @param subjects a subjects that can own some objects
  * @param disposition a map that represents owners of an objects
  */
case class Distribution(objects: Set[Object], subjects: Set[Subject], disposition: Map[Object, Option[Subject]])
