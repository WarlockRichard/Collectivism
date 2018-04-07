package bwsw.testcase

import org.scalatest.FunSpec

class integrationTest extends FunSpec{
  describe("An integration test") {
    val objects = Set(
      Object("0"),
      Object("1"),
      Object("2"),
      Object("3"),
      Object("4"),
      Object("5"),
      Object("6"),
      Object("7"),
      Object("8"),
      Object("9"))

    val baseDistribution = OwnershipDistributor.createDistribution(objects)

    val subjectS1 = Subject("S1", false, Set(Object("2"), Object("3")))
    val subjectS2 = Subject("S2", false, Set(Object("2"), Object("4"), Object("5")))
    val subjectS3 = Subject("S3", false, Set(Object("2")))
    val subjectS4 = Subject("S4", true, Set(Object("2"), Object("3")))

    it("can pass an integration test"){
      assertResult(Distribution(objects, Set(subjectS1, subjectS3, subjectS4), Map(
        Object("0") -> None,
        Object("1") -> None,
        Object("2") -> Some(subjectS3),
        Object("3") -> Some(subjectS1),
        Object("4") -> None,
        Object("5") -> None,
        Object("6") -> None,
        Object("7") -> None,
        Object("8") -> None,
        Object("9") -> None))){
      val distribution1 = OwnershipDistributor.addSubject(subjectS1, baseDistribution)
      val distribution2 = OwnershipDistributor.addSubject(subjectS2, distribution1)
      val distribution3 = OwnershipDistributor.addSubject(subjectS3, distribution2)

      val distribution4 = OwnershipDistributor.removeSubject(subjectS3, distribution3)

      val distribution5 = OwnershipDistributor.addSubject(subjectS4, distribution4)
      }
    }
//    it("can remove subject"){
//    }
  }
}
