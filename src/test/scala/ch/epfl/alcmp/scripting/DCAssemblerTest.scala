package ch.epfl.alcmp.scripting

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest._
import matchers._

class DCAssemblerTest extends AnyFlatSpec with should.Matchers {

  "Reading content" should "return null string iff path is invalid" in {
    DCAssembler.readTemplate("foo.py") should be (null)
    DCAssembler.readTemplate should not be (null)
  }
}
