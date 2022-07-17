package ch.epfl.alcmp.net

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

import scala.util.Random

class SerdeTest extends AnyFlatSpec with should.Matchers {

  "Serializing and Deserializing numbers" should "work" in {
    val n = 456
    Serde.NUMBER.serialize(n) should be ("456")

    val random = new Random()
    for i <- 0 to 100 do {
      val r = random.nextInt()
      Serde.NUMBER.deserialize(Serde.NUMBER.serialize(r)) should be (r)
    }
  }

  "Deserializing a serialized text" should "give back the original text" in {
    val text = "Hello, World!"
    Serde.TEXT.deserialize(Serde.TEXT.serialize(text)) should be (text)
  }
}
