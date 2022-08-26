package ch.epfl.alcmp.simulation

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should
import java.io.File

class SimulatorTest extends AnyFlatSpec with should.Matchers {

  "Running python script" should "work" in {
    val input = "5,8,3,7,10,23,16,2"
    val divide = "return arg[:n//2], arg[n//2:]"
    val combine = "return [arg0[0]] if arg0[0] > arg1[0] else [arg1[0]]"
    val base = "if n == 1: return arg"
    val simulator = new Simulator()
    val process = simulator.runSimulator(base, divide, combine, "ListType", "3999", input)
    process.destroy()
  }
}