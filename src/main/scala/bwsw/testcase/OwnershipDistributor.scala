package bwsw.testcase

import scala.annotation.tailrec

/** Can create new Distribution by adding or removing
  * Subject by one at a time maintaining distributive justice
  *
  */
object OwnershipDistributor {

  /** Create base distribution where all given objects are free*/
  def createDistribution(
            objects: Set[Object] = Set(
              Object("1"),
              Object("5"),
              Object("2"),
              Object("3"),
              Object("4"))//TODO remove stub
          ) = Distribution(objects, Set.empty[Subject], objects.map((_, Option.empty[Subject])).toMap)

  /** Form a new distribution with that Subject
    *
    * Adds a Subject to a set of already existing ones
    * Distribute necessary free objects to that subject
    * Redistribute necessary objects from subject with lower priority to that subject
    * Redistribute objects between subjects of same priority for distribution to be fair
    * @param that - a Subject to add
    * @param distribution old Distribution
    * @return new Distribution
    */
  def addSubject(that: Subject, distribution: Distribution): Distribution = {
    implicit val inspector: Inspector = AdditionInspector
    implicit val subjects: Set[Subject] = distribution.subjects + that

    //appoint free objects
    val emptinessBasedDisposition = distribution.disposition.map {
        case (anObject, None) => if (that.objects.contains(anObject)) (anObject, Some(that)) else (anObject, None)
        case (anObject, Some(subject)) => (anObject, Some(subject))
    }

    //redistribution from subjects of low priority
    val priorityBasedDisposition =
      if(that.lowPriority)
        emptinessBasedDisposition
      else
        shiftN(that, emptinessBasedDisposition)(inspector.searchDonationByPriority)

    //balancing of subjects with the same priority
    val justlyDisposition = shiftN(that, priorityBasedDisposition)(inspector.searchDonationByBalance)
    Distribution(
      distribution.objects,
      subjects,
      justlyDisposition
    )
  }

  /** Form a new distribution without that Subject
    *
    * Removes a Subject form set of already existing ones
    * Redistribute objects belonged to that subject between subjects of normal priority
    * Redistribute remaining objects between subjects with low priority
    * Set free remaining objects
    * @param that - a Subject to remove
    * @param distribution old Distribution
    * @return new Distribution
    */
  def removeSubject(that: Subject, distribution: Distribution): Distribution = {
    implicit val inspector: Inspector = RemovalInspector
    implicit val subjects: Set[Subject] = distribution.subjects - that

    //balance objects of subjects of the same priority
    val balanceBasedDisposition = shiftN(that, distribution.disposition)(inspector.searchDonationByBalance)

    val priorityBasedDisposition =
      if(that.lowPriority)
        balanceBasedDisposition
      else
        //give away objects to subjects of low priority
        shiftN(that, balanceBasedDisposition)(inspector.searchDonationByPriority)


    //set remaining objects free
    val justlyDisposition = priorityBasedDisposition.map {
      case (anObject, Some(aSubject)) => if(aSubject == that) (anObject, None) else (anObject, Some(aSubject))
      case (anObject, None) => (anObject, None)
    }

    Distribution(
      distribution.objects,
      subjects,
      justlyDisposition
    )
  }

  /** Recursively redistributes an objects maintaining distributive justice
    * to or from the subject depending on given inspector
    *
    * In a single call redistributes a single object
    * @param subject a subject for which donation is intended
    * @param disposition a map that represents owners of an objects
    * @param searchMethod method using to find an object to redistribute in favor of the subject
    * @param inspector provides a methods of object searching
    * @return distribution with a single object redistributed
    *         or distribution obtained as parameter if distribution is fair
    */
  @tailrec
  private def shiftN(
        subject: Subject,
        disposition: Map[Object, Option[Subject]])
      (searchMethod: (Subject, Map[Object, Option[Subject]], Set[Subject]) => Option[(Subject, Object, Subject)])
      (implicit inspector: Inspector,  subjects: Set[Subject]): Map[Object, Option[Subject]] = {
    val donationMaybe = searchMethod(subject, disposition, subjects)
    donationMaybe match {
      case Some((donee, donation, subjectToVerify)) =>
        val newDisposition = disposition + (donation -> Some(donee))
        shiftN(subject, shift1(subjectToVerify, newDisposition))(searchMethod)
      case None =>
        disposition
    }
  }

  /** Redistributes an objects in previously fairly distributed subset
    *
    * Method can be called after the redistribution of a single object inside shiftN method
    * This method relies that balance of that subject's connected component
    * (excluding the subject given in shiftN method) could have been violated only by 1
    * @param subject a subject balance of which could have been changed
    * @param disposition a map that represents owners of an objects
    * @param inspector provides a methods of object searching
    * @return distribution with a single object redistributed
    *         or distribution obtained as parameter if distribution is fair for given subset
    */
  @tailrec
  private def shift1(
       subject: Subject,
       disposition: Map[Object, Option[Subject]])
     (implicit inspector: Inspector,  subjects: Set[Subject]): Map[Object, Option[Subject]] = {
    val donationMaybe = inspector.searchDonationByBalance(subject, disposition, subjects)
    donationMaybe match {
      case Some((donee, donation, subjectToVerify)) =>
        val newDisposition = disposition + (donation -> Some(donee))
        shift1(subjectToVerify, newDisposition)
      case None =>
        disposition
    }
  }

  /** Tests Distribution for compliance of distributive justice
    *
    * Formally tests if Distribution can be balanced further
    */
  def testForJustice(distribution: Distribution): Boolean = {
    !distribution.subjects.exists { subject => {
      val canAssignEmptyObject = distribution.disposition.exists {
        case (anObject, None) => subject.objects.contains(anObject)
        case _ => false
      }
      implicit val subjects: Set[Subject] = distribution.subjects
      implicit val inspector: Inspector = AdditionInspector
      val canRedistibuteObjectsFromLowPriority = if(!subject.lowPriority) distribution != this.shiftN(subject, distribution.disposition)(AdditionInspector.searchDonationByPriority) else false
      val canRedistibuteObjects = distribution != this.shiftN(subject, distribution.disposition)(AdditionInspector.searchDonationByBalance)
      canAssignEmptyObject || canRedistibuteObjects || canRedistibuteObjectsFromLowPriority
    }}
  }
}