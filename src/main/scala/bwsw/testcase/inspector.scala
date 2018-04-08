package bwsw.testcase

sealed trait Inspector{
  def searchDonationByBalance(subject: Subject, disposition: Map[Object, Option[Subject]], subjects: Set[Subject]): Option[(Subject, Object, Subject)]
  def searchDonationByPriority(subject: Subject, disposition: Map[Object, Option[Subject]], subjects: Set[Subject]): Option[(Subject, Object, Subject)]
}

object AdditionInspector extends Inspector {

  /** Returns a subject-donor, an object to donate and a subject-donee if such exists
    * Subjects must have same priority and object can be justly redistributed from one to another
    *
    * Donor is the subject with maximum overall owned object quantity
    * one of the subjects with at least one object that can be redistributed in favor of the donee
    * @param donee a subject for which donation is intended
    * @param disposition a map that represents owners of an objects
    * @return Some Tuple3 of possible subject-donor, donated object and subject-donee if such exists
    *         None otherwise
    */
  def searchDonationByBalance(
        donee: Subject,
        disposition: Map[Object, Option[Subject]],
        subjects: Set[Subject]
      ): Option[(Subject, Object, Subject)] = {
    val stakeholderObjects = disposition.foldLeft(Map[Subject, Set[Object]]()){
        case (propertyMap, (anObject, Some(aSubject))) =>
          if(aSubject.lowPriority == donee.lowPriority){
            propertyMap + (aSubject -> (propertyMap.getOrElse(aSubject, Set.empty[Object]) + anObject))
          }
          else
            propertyMap
        case (propertyMap, (_, None)) => propertyMap
    }

    val stakeholders = stakeholderObjects.filter{ case (aSubject, anObjects) => anObjects.intersect(donee.objects).nonEmpty }
    if ((stakeholders - donee).isEmpty) {
      return None
    }
    val (topDonor, topDonorPower) = stakeholders.maxBy { _._2.size }

    if (topDonor != donee
      && topDonorPower.size > stakeholders.getOrElse(donee, Set.empty[Object]).size + 1 /*unfairness criteria*/) {
      val donation = stakeholders(topDonor).intersect(donee.objects).head
      Some((donee, donation, topDonor))
    }
    else
      None
  }


  /** Returns a subject-donor, an object to donate and a subject-donee if such exists
    * Subject-donee have higher priority and object must be redistributed from subject with lower one
    *
    * Subject with low priority can only own an object if there is no subjects with
    * a normal priority that can own this object
    * @param donee a Subject for which donation is intended
    * @param disposition a Map that represents owners of an objects
    * @return Some Tuple3 of possible subject-donor, donated object and subject-donee if such exists
    *         None otherwise
    */
  def searchDonationByPriority(
        donee: Subject,
        disposition: Map[Object, Option[Subject]],
        subjects: Set[Subject]
      ): Option[(Subject, Object, Subject)] = {
    disposition
      .find {
        case (k, Some(v)) => donee.objects.contains(k) && (v.lowPriority > donee.lowPriority)
        case (k, None) => false
      }.map { case (donation, Some(donor)) => (donee, donation, donor) }
  }
}

object RemovalInspector extends Inspector{

  /** Returns a subject-donor, an object to donate and a subject-donee if such exists
    * Subjects must have same priority and object can be justly redistributed from one to another
    *
    * Donee is the subject with the least overall owned object quantity
    * one of the subjects with at least one object that can be redistributed in favor of the donee
    * @param donor a subject which must donate his objects
    * @param disposition a map that represents owners of an objects
    * @return Some Tuple3 of possible subject-donor, donated object and subject-donee if such exists
    *         None otherwise
    */
  def searchDonationByBalance(
        donor: Subject,
        disposition: Map[Object, Option[Subject]],
        subjects: Set[Subject]
      ): Option[(Subject, Object, Subject)] = {
    val objectsToDonate = disposition.filter { _._2.contains(donor) }.keySet

    val contenderObjects = disposition.foldLeft(Map[Subject, Set[Object]]()){
      case (propertyMap, (anObject, Some(aSubject))) =>
        if(aSubject.lowPriority == donor.lowPriority)
          propertyMap + (aSubject -> (propertyMap.getOrElse(aSubject, Set.empty[Object]) + anObject))
        else
          propertyMap
      case (propertyMap, (_, None)) => propertyMap
    }
    val contenders = subjects.filter { subject => subject.objects.intersect(objectsToDonate).nonEmpty && subject.lowPriority == donor.lowPriority  && subject != donor}
    if (contenders.nonEmpty) {
      val donee = contenders.minBy( contenderObjects.getOrElse(_, Set.empty[Object]).size )
      val donation = donee.objects.intersect(objectsToDonate).head
      Some((donee, donation, donee))
    }
    else
      None
  }

  /** Returns a subject-donor, an object to donate and a subject-donee if such exists
    * Subject-donor have normal priority and there is no other subjects of same priority
    * so object must be redistributed between subjects with low priority
    *
    * Subject with low priority can only own an object if there is no subjects with
    * a normal priority that can own this object
    * @param donor a subject which must donate his objects
    * @param disposition a Map that represents owners of an objects
    * @return Some Tuple3 of possible subject-donor, donated object and subject-donee if such exists
    *         None otherwise
    */
  def searchDonationByPriority(
      donor: Subject,
      disposition: Map[Object, Option[Subject]],
      subjects: Set[Subject]
    ): Option[(Subject, Object, Subject)] = {

    val objectsToDonate = disposition.filter{ _._2.contains(donor) }.keySet

    val contenderObjects = disposition.foldLeft(Map[Subject, Set[Object]]()){
      case (propertyMap, (anObject, Some(aSubject))) =>
        if(aSubject.lowPriority > donor.lowPriority)
          propertyMap + (aSubject -> (propertyMap.getOrElse(aSubject, Set.empty[Object]) + anObject))
        else propertyMap
      case (propertyMap, (_, None)) => propertyMap
    }
    val contenders = subjects.filter{ subject => subject.objects.intersect(objectsToDonate).nonEmpty && subject.lowPriority > donor.lowPriority && subject != donor}
    if (contenders.nonEmpty) {
      val donee = contenders.minBy( contenderObjects.getOrElse(_, Set.empty[Object]).size )
      val donation = donee.objects.intersect(objectsToDonate).head
      Some((donee, donation, donee))
    }
    else
      None
  }
}