package ch.epfl.alcmp.net

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
    val reg1 = new RegisterMessage(57)
    Serdes.serialize[RegisterMessage](reg1) should be ("57")
    Serdes.deserialize[RegisterMessage]("68") should be (RegisterMessage(68))
  }

  "Serializing and Deserializing DONE messages" should "work" in {
    val reg1 = new DoneMessage(57456)
    Serdes.serialize[DoneMessage](reg1) should be ("57456")
    Serdes.deserialize[DoneMessage]("68") should be (DoneMessage(68))
  }
}
