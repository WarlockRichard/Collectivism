package bwsw.testcase

import org.scalatest.FunSpec

class inspectorTest extends FunSpec{
  describe("An AdditionInspector") {
    val subject1 = Subject("1", false, Set(Object("1"), Object("3"), Object("5")))
    val subject2 = Subject("2", false, Set(Object("3"), Object("4")))
    val subject3 = Subject("3", true, Set(Object("3"), Object("1")))
    it("can find a donation by balance") {
      assertResult(Some(subject1, Object("3"), subject2)) {
        AdditionInspector.searchDonationByBalance(
          subject1,
          Map(
            (Object("3"), Some(subject2)),
            (Object("1"), Some(subject3)),
            (Object("4"), Some(subject2)),
            (Object("2"), None)),
          Set(subject1, subject2, subject3))
      }
    }

    it("can find a donation by priority") {
      assertResult(Some(subject1, Object("1"), subject3)) {
        AdditionInspector.searchDonationByPriority(
          subject1,
          Map(
            (Object("3"), Some(subject2)),
            (Object("1"), Some(subject3)),
            (Object("4"), None)),
          Set(subject1, subject2, subject3))
      }
    }
  }

  describe("An RemovalInspector") {
    val subject1 = Subject("1", false, Set(Object("2"), Object("3"), Object("5")))
    val subject2 = Subject("2", true, Set(Object("3"), Object("1")))
    val subject3 = Subject("3", false, Set(Object("1")))
    it("can find a donation by balance") {
      assertResult(Some(subject3, Object("1"), subject3)) {
        RemovalInspector.searchDonationByBalance(
          subject1,
          Map(
            (Object("3"), Some(subject2)),
            (Object("1"), Some(subject1)),
            (Object("4"), None)),
          Set(subject2, subject3))
      }
    }

    it("can find a donation by priority") {
      assertResult(Some(subject2, Object("3"), subject2)) {
        fail()
//        AdditionInspector.searchDonationByPriority(
//          Subject("1", false, Set(Object("2"), Object("3"), Object("5"))),
//          Map(
//            (Object("3"), Some(Subject("2", true, Set(Object("3"), Object("1"))))),
//            (Object("1"), Some(Subject("3", false, Set(Object("3"), Object("1"))))),
//            (Object("4"), None)),
//          Set(
//            Subject("1", false, Set(Object("2"), Object("3"), Object("5"))),
//            Subject("2", true, Set(Object("3"), Object("1"))),
//            Subject("3", false, Set(Object("3"), Object("1"))))))
      }
    }
  }
}
