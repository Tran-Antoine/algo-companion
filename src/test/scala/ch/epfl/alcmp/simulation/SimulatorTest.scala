package ch.epfl.alcmp.simulation

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should
import java.io.File

class SimulatorTest extends AnyFlatSpec with should.Matchers {

    "Running python script" should "work" ignore {
      val simulator = new Simulator()
      val process = simulator.runSimulator("", "", "", "", "", "")
      simulator.stopSimulator(process)
    }
}
