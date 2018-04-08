package bwsw.testcase

import org.scalatest.FunSpec

class inspectorTest extends FunSpec {
  describe("An AdditionInspector") {
    val subject1 = Subject("1", lowPriority = false, Set(Object("1"), Object("3"), Object("5")))
    val subject2 = Subject("2", lowPriority = false, Set(Object("3"), Object("4")))
    val subject3 = Subject("3", lowPriority = true, Set(Object("3"), Object("1")))

    //AdditionInspector.searchDonationByBalance tests
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
    it("searchDonationByBalance returns None if exists only donation by priority") {
      assertResult(None) {
        AdditionInspector.searchDonationByBalance(
          subject1,
          Map(
            (Object("3"), Some(subject1)),
            (Object("1"), Some(subject3)),
            (Object("4"), Some(subject2)),
            (Object("2"), None)),
          Set(subject1, subject3))
      }
    }
    it("searchDonationByBalance returns None if Map is empty") {
      assertResult(None) {
        AdditionInspector.searchDonationByBalance(
          subject1,
          Map(),
          Set(subject1, subject2, subject3))
      }
    }
    it("searchDonationByBalance returns None if Map and Set[Subject] is empty") {
      assertResult(None) {
        AdditionInspector.searchDonationByBalance(subject1, Map(), Set())
      }
    }
    it("searchDonationByBalance returns None if subject LOWPRIO and all object are held by subjects with a normal priority") {
      assertResult(None) {
        AdditionInspector.searchDonationByBalance(subject3,
          Map(
            (Object("3"), Some(subject2)),
            (Object("1"), Some(subject1)),
            (Object("4"), Some(subject2)),
            (Object("2"), None)),
          Set(subject1, subject2, subject3))
      }
    }
    it("searchDonationByBalance returns None if distributive justice is achieved") {
      assertResult(None) {
        AdditionInspector.searchDonationByBalance(subject1,
          Map(
            (Object("3"), Some(subject2)),
            (Object("1"), Some(subject1)),
            (Object("4"), Some(subject2)),
            (Object("2"), None)),
          Set(subject1, subject2, subject3))
      }
    }
    it("searchDonationByBalance returns None if donee has the most power") {
      assertResult(None) {
        AdditionInspector.searchDonationByBalance(subject1,
          Map(
            (Object("3"), Some(subject1)),
            (Object("1"), Some(subject1)),
            (Object("4"), Some(subject2)),
            (Object("2"), None)),
          Set(subject1, subject2, subject3))
      }
    }
    //AdditionInspector.searchDonationByBalance tests end

    //AdditionInspector.searchDonationByPriority tests
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
    //AdditionInspector.searchDonationByPriority tests end
  }

  describe("A RemovalInspector") {
    val subject1 = Subject("1", lowPriority = false, Set(Object("1"), Object("4"), Object("5")))
    val subject2 = Subject("2", lowPriority = true, Set(Object("3"), Object("1")))
    val subject3 = Subject("3", lowPriority = false, Set(Object("1")))
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
      assertResult(Some(subject2, Object("1"), subject2)) {
        RemovalInspector.searchDonationByPriority(
          subject1,
          Map(
            (Object("3"), Some(subject1)),
            (Object("1"), Some(subject3)),
            (Object("4"), None)),
          Set(subject2, subject3))
      }
    }
  }
}

