package ch.epfl.alcmp.net

import ch.epfl.alcmp.data.IList
import ch.epfl.alcmp.data.TypeId
import ch.epfl.alcmp.net.SimulationMessage.{CombineMessage, DivideMessage, DoneMessage, RegisterMessage}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

import scala.util.Random

class SerdeTest extends AnyFlatSpec with should.Matchers {

  "Serializing and Deserializing numbers" should "work" in {
    val n = 456
    Serdes.serialize[Int](n) should be ("456")

    val random = new Random()
    for i <- 0 to 100 do {
      val r = random.nextInt()
      Serdes.deserialize[Int](Serdes.serialize[Int](r)) should be (r)
    }
  }

  "Deserializing a serialized text" should "give back the original text" in {
    val text = "Hello, World!"
    Serdes.deserialize[String](Serdes.serialize[String](text)) should be (text)
  }

  "Serializing and Deserializing REGISTER messages" should "work" in {
    val reg1 = RegisterMessage(57)
    Serdes.serialize[RegisterMessage](reg1) should be ("57")
    Serdes.deserialize[RegisterMessage]("68") should be (RegisterMessage(68))
  }

  "Serializing and Deserializing DONE messages" should "work" in {
    val reg1 = DoneMessage(57456)
    Serdes.serialize[DoneMessage](reg1) should be ("57456")
    Serdes.deserialize[DoneMessage]("68") should be (DoneMessage(68))
  }

  "Serializing and Deserializing COMBINE messages" should "work" in {
    val reg1 = CombineMessage(101, 2, 1, IList(List(5, 6, 7)), List(0, 1, 2))
    val reg2 = CombineMessage(102, 3, 2, IList(List(6, 7, 8)), List(1, 2, 3))
    Serdes.serialize[CombineMessage](TypeId.ListType, reg1) should be ("101/2/1/5,6,7/0,1,2")
    Serdes.serialize[CombineMessage](TypeId.ListType, reg2) should be ("102/3/2/6,7,8/1,2,3")
    Serdes.deserialize[CombineMessage](TypeId.ListType, "1/2/3/0,9,10/0,1,2") should be (CombineMessage(1, 2, 3, IList(List(0, 9, 10)), List(0, 1, 2)))
    Serdes.deserialize[CombineMessage](TypeId.ListType, "0/0/0/0,0,0/0,0,0") should be (CombineMessage(0, 0, 0, IList(List(0, 0, 0)), List(0, 0, 0)))

  }
}
