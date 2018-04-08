package bwsw.testcase

import org.scalatest.FunSpec


class OwnershipDistributorTest extends FunSpec{
  describe("An OwnershipDistribution") {
    it("can create new distribution from set of objects") {
      OwnershipDistributor.createDistribution(
        Set(
          Object("1"),
          Object("5"),
          Object("2"),
          Object("3"),
          Object("4")))
    }
    it("can create new distribution from empty set of objects") {
      OwnershipDistributor.createDistribution(Set())
    }

    val baseDistribution = Distribution(
      Set(
        Object("1"),
        Object("5"),
        Object("2"),
        Object("3"),
        Object("4")),
      Set.empty[Subject],
      Map(
        Object("1") -> None,
        Object("5") -> None,
        Object("2") -> None,
        Object("3") -> None,
        Object("4") -> None)
    )
    it("can test distribution for justice") {
      assertResult(true){
        OwnershipDistributor.testForJustice(baseDistribution)
      }
      assertResult(false){
        OwnershipDistributor.testForJustice(Distribution(Set(Object("1")), Set(Subject("1", lowPriority = false, Set(Object("1")))), Map(Object("1") -> None)))
      }
    }

    val subject1 = Subject("1", lowPriority = false, Set(Object("2"), Object("3"), Object("5")))
    val subject2 = Subject("1", lowPriority = true, Set(Object("1"), Object("3"), Object("4")))
    it("can add a subject with normal priority") {
      val distributorWithSubject1 =  OwnershipDistributor.addSubject(subject1, baseDistribution)
    }
    it("can add a subject with low priority") {
      val distributorWithLowPrioritySubject2 =  OwnershipDistributor.addSubject(subject2, baseDistribution)
    }

    val filledDistribution = Distribution(
      Set(
        Object("1"),
        Object("5"),
        Object("2"),
        Object("3"),
        Object("4")),
      Set(
        subject1,
        subject2
      ),
      Map(
        Object("1") -> Some(subject2),
        Object("5") -> Some(subject1),
        Object("2") -> Some(subject1),
        Object("3") -> Some(subject1),
        Object("4") -> Some(subject2))
    )
    it("can remove a subject with normal priority") {
      val distributorWithoutSubject1 =  OwnershipDistributor.addSubject(subject1, filledDistribution)
    }
    it("can remove a subject with low priority") {
      val distributorWithoutSubject2 =  OwnershipDistributor.addSubject(subject2, filledDistribution)
    }
  }
}
