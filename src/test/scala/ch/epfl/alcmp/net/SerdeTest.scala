package ch.epfl.alcmp.net

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
}
