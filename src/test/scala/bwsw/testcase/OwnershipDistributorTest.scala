package bwsw.testcase

import org.scalatest.FunSpec


class OwnershipDistributorTest extends FunSpec{
  describe("An OwnershipDistribution") {
    val distribution = OwnershipDistributor.createDistribution(
      Set(
        Object("1"),
        Object("5"),
        Object("2"),
        Object("3"),
        Object("4")))

    it("can create new distribution from set of objects") {
      OwnershipDistributor.createDistribution(
        Set(
          Object("1"),
          Object("5"),
          Object("2"),
          Object("3"),
          Object("4")))
    }

    it("can test distribution for justice") {
      assertResult(true){
        OwnershipDistributor.testForJustice(distribution)
      }
      assertResult(false){
        OwnershipDistributor.testForJustice(Distribution(Set(Object("1")), Set(Subject("1", false, Set(Object("1")))), Map(Object("1") -> None)))
      }
    }


//    it("can add a subject") {
//      val distributorWithOneSubject =  OwnershipDistributor.addSubject(Subject("1", true, Set(Object("2"), Object("3"), Object("5"))), distribution)
//    }
//
//    it("can add more subjects") {
//      val distributionWithOneSubject =  OwnershipDistributor.addSubject(Subject("1", true, Set(Object("2"), Object("3"), Object("5"))), distribution)
//      val distributionWithTwoSubjects = OwnershipDistributor.addSubject(Subject("2", true, Set(Object("3"), Object("1"), Object("5"), Object("2"))), distributionWithOneSubject)
//    }

//    it("can be instantiated with a set of objects"){pending}
//    it("can not be instantiated using primary constructor"){pending}
//    it("can add a subject to a distribution"){pending}
//    it("can remove a subject from a distribution"){pending}
  }
}
