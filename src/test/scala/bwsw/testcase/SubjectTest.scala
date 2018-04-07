package bwsw.testcase

import org.scalatest._

class SubjectTest extends FunSpec {
  describe("A Subject") {
    it("can have a set of desirable objects") {
      val subject = Subject("Dude", true, Set(Object("Carpet"), Object("White russian")))
    }
  }
}
