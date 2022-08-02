package ch.epfl.alcmp.data

import ch.epfl.alcmp.net.SimulationMessage.DivideMessage
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.*
import matchers.*

class SimulationDataTest extends AnyFlatSpec with should.Matchers {


  "Adding simple division data" should "fill an entire row correctly" in {
    val data = new SimulationData
    val left = IList(List(1, 2, 3))
    val right = IList(List(4, 5, 6))

    val message1 = DivideMessage(0, 1, 0, List(left), Nil)
    val message2 = DivideMessage(0, 1, 1, List(right), Nil)

    data.addDivisionData(message1)
    data.addDivisionData(message2)


    val result = data.divisionRowAt(1).flatten(x => x.asInstanceOf[IList].list)

    result should be (List(1, 2, 3, 4, 5, 6))
  }

  "Adding multiple division data" should "fill an entire row correctly" in {
    val data = new SimulationData
    val left1 = IList(List(1, 2))
    val left2 = IList(List(3))

    val right1 = IList(List(4, 5))
    val right2 = IList(List(6))

    val message1 = DivideMessage(0, 1, 0, List(left1, left2), Nil)
    val message2 = DivideMessage(0, 1, 2, List(right1, right2), Nil)

    data.addDivisionData(message1)
    data.addDivisionData(message2)


    val result = data.divisionRowAt(1).flatten(x => x.asInstanceOf[IList].list)

    result should be (List(1, 2, 3, 4, 5, 6))
  }
}
