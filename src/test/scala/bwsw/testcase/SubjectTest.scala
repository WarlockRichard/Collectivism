package bwsw.testcase

import org.scalatest._

class SubjectTest extends FunSpec {
  describe("A Subject") {
    it("can be instantiated") {
      val subject = Subject("Dude", lowPriority = true, Set(Object("Carpet"), Object("White russian")))
    }
  }
}
