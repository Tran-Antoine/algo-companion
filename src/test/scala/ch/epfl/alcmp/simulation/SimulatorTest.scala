package ch.epfl.alcmp.simulation

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

class SimulatorTest extends AnyFlatSpec with should.Matchers {

    "Creating dc script" should "work perfectly fine" in {

      val divide = "return arg[:n//2], arg[n//2:]"
      val combine = "return [arg0[0]] if arg0[0] > arg1[0] else [arg1[0]]"
      val base = "if n == 1: return arg"

      val simulator = new Simulator()
      simulator.makeDCScript(base, divide, combine)
    }
}
