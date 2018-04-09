package bwsw.testcase

import org.scalatest.FunSpec

class inspectorTest extends FunSpec {
  describe("An AdditionInspector") {
    val subject1 = Subject("1", lowPriority = false, Set(Object("1"), Object("3"), Object("5")))
    val subject2 = Subject("2", lowPriority = false, Set(Object("3"), Object("4")))
    val subject3 = Subject("3", lowPriority = true, Set(Object("3"), Object("1"), Object("6")))
    val subject4 = Subject("4", lowPriority = true, Set(Object("3"), Object("1"), Object("5")))

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
    it("searchDonationByBalance returns None if subject LOWPRIO and all objects are held by subjects with a normal priority") {
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
    it("searchDonationByBalance returns None if donee have the most power") {
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
            (Object("4"), None),
            (Object("2"), None)),
          Set(subject1, subject2, subject3))
      }
    }
    it("searchDonationByPriority returns None if Map is empty") {
      assertResult(None) {
        AdditionInspector.searchDonationByPriority(
          subject1,
          Map(),
          Set(subject1, subject2, subject3))
      }
    }
    it("searchDonationByPriority returns None if donation can't be found") {
      assertResult(None) {
        AdditionInspector.searchDonationByPriority(
          subject1,
          Map(
            (Object("3"), Some(subject2)),
            (Object("1"), Some(subject1)),
            (Object("6"), Some(subject3)),
            (Object("2"), None)),
          Set(subject1, subject2, subject3))
      }
    }
    //AdditionInspector.searchDonationByPriority tests end
  }

  describe("A RemovalInspector") {
    val subject1 = Subject("1", lowPriority = false, Set(Object("1"), Object("4"), Object("5")))
    val subject2 = Subject("2", lowPriority = true, Set(Object("3"), Object("1")))
    val subject3 = Subject("3", lowPriority = false, Set(Object("1")))
    val subject4 = Subject("4", lowPriority = true, Set(Object("1"), Object("5")))

    //RemovalInspector.searchDonationByBalance tests
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
    it("searchDonationByBalance returns None if donor have no objects") {
      assertResult(None) {
        RemovalInspector.searchDonationByBalance(
          subject1,
          Map(
            (Object("3"), Some(subject2)),
            (Object("1"), Some(subject3)),
            (Object("4"), None)),
          Set(subject2, subject3))
      }
    }
    it("searchDonationByBalance returns None if Map is empty") {
      assertResult(None) {
        RemovalInspector.searchDonationByBalance(
          subject1,
          Map(),
          Set(subject2, subject3))
      }
    }
    it("searchDonationByBalance returns None if Map is empty and there is no subjects") {
      assertResult(None) {
        RemovalInspector.searchDonationByBalance(
          subject1,
          Map(),
          Set())
      }
    }
    it("searchDonationByBalance returns None if there is no subjects") {
      assertResult(None) {
        RemovalInspector.searchDonationByBalance(
          subject1,
          Map(
            (Object("3"), Some(subject1)),
            (Object("1"), Some(subject1)),
            (Object("5"), Some(subject1))),
          Set())
      }
    }
    it("searchDonationByBalance returns None if there is no donee") {
      assertResult(None) {
        RemovalInspector.searchDonationByBalance(
          subject1,
          Map(
            (Object("3"), Some(subject3)),
            (Object("1"), Some(subject2)),
            (Object("5"), Some(subject1))),
          Set(subject2, subject3))
      }
    }
    it("searchDonationByBalance returns None if there is no donee of the same priority") {
      assertResult(None) {
        RemovalInspector.searchDonationByBalance(
          subject1,
          Map(
            (Object("3"), Some(subject2)),
            (Object("1"), Some(subject2)),
            (Object("5"), Some(subject1))),
          Set(subject2, subject3, subject4))
      }
    }
    //RemovalInspector.searchDonationByBalance tests end

    //RemovalInspector.searchDonationByPriority tests
    it("can find a donation by priority") {
      assertResult(Some(subject4, Object("5"), subject4)) {
        RemovalInspector.searchDonationByPriority(
          subject1,
          Map(
            (Object("3"), Some(subject2)),
            (Object("1"), Some(subject3)),
            (Object("5"), Some(subject1))),
          Set(subject2, subject3, subject4))
      }
    }
    it("searchDonationByPriority returns None if donor have no objects") {
      assertResult(None) {
        RemovalInspector.searchDonationByPriority(
          subject1,
          Map(
            (Object("3"), Some(subject2)),
            (Object("1"), Some(subject3)),
            (Object("4"), None)),
          Set(subject2, subject3, subject4))
      }
    }
    it("searchDonationByPriority returns None if Map is empty") {
      assertResult(None) {
        RemovalInspector.searchDonationByPriority(
          subject1,
          Map(),
          Set(subject2, subject3))
      }
    }
    it("searchDonationByPriority returns None if Map is empty and there is no subjects") {
      assertResult(None) {
        RemovalInspector.searchDonationByBalance(
          subject1,
          Map(),
          Set())
      }
    }
    it("searchDonationByPriority returns None if there is no subjects") {
      assertResult(None) {
        RemovalInspector.searchDonationByPriority(
          subject1,
          Map(
            (Object("3"), Some(subject1)),
            (Object("1"), Some(subject1)),
            (Object("5"), Some(subject1))),
          Set())
      }
    }
    it("searchDonationByPriority returns None if there is no donee") {
      assertResult(None) {
        RemovalInspector.searchDonationByPriority(
          subject1,
          Map(
            (Object("3"), Some(subject3)),
            (Object("1"), Some(subject2)),
            (Object("5"), Some(subject1))),
          Set(subject2, subject3))
      }
    }
    it("searchDonationByPriority returns None if there is only donee of the same priority") {
      assertResult(None) {
        RemovalInspector.searchDonationByPriority(
          subject2,
          Map(
            (Object("3"), Some(subject2)),
            (Object("1"), Some(subject2)),
            (Object("5"), Some(subject4))),
          Set(subject4))
      }
    }
    //RemovalInspector.searchDonationByPriority tests end
  }
}

