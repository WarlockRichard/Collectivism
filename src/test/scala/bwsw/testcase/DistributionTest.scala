package bwsw.testcase

import org.scalatest.FunSpec

class DistributionTest extends FunSpec {
  describe("A Distribution") {
    it("can be instantiated") {
      val distribution = Distribution(
        Set(Object("1")),
        Set(Subject("Dude", lowPriority = true, Set(Object("Carpet"), Object("White russian")))),
        Map(
          Object("1") -> None
        ))
    }
  }
}
