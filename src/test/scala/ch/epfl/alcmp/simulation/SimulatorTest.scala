package ch.epfl.alcmp.simulation

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should
import java.io.File

class SimulatorTest extends AnyFlatSpec with should.Matchers {

    val simulator = new Simulator()

    "Creating dc script" should "work" in {

      val divide = "return arg[:n//2], arg[n//2:]"
      val combine = "return [arg0[0]] if arg0[0] > arg1[0] else [arg1[0]]"
      val base = "if n == 1: return arg"

      simulator.makeDCScript(base, divide, combine)

      assert(File("src/main/bin/dc_script.py").exists())
    }

    "Copying matrix file to bin folder" should "work" in {
      simulator.copyToBin("/matrix.py")

      assert(File("src/main/bin/matrix.py").exists())
    }

    "Running python script" should "work" in {
      simulator.runSimulator()
    }
}
