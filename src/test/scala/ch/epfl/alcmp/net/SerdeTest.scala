package ch.epfl.alcmp.net

import ch.epfl.alcmp.data.{IList, IMatrix, IHeap, IBinaryTree}
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
    val comb1 = CombineMessage(101, 2, 1, IList(List(5, 6, 7)), List(0, 1, 2))
    Serdes.serialize[CombineMessage](TypeId.ListType, comb1) should be ("101/2/1/0,1,2/5,6,7")
    Serdes.deserialize[CombineMessage](TypeId.ListType, "101/2/1/0,1,2/5,6,7") should be (comb1)

    val comb2 = CombineMessage(101, 2, 1, IMatrix(List(List(1, 0), List(0, 1))), List(0, 1, 2))
    Serdes.serialize[CombineMessage](TypeId.MatrixType, comb2) should be ("101/2/1/0,1,2/1,0,,0,1")
    Serdes.deserialize[CombineMessage](TypeId.MatrixType, "101/2/1/0,1,2/1,0,,0,1") should be (comb2)

    val comb3 = CombineMessage(102, 3, 2, IHeap(List(6, 7, 8)), List(1, 2, 3))
    Serdes.serialize[CombineMessage](TypeId.HeapType, comb3) should be ("102/3/2/1,2,3/6,7,8")
    Serdes.deserialize[CombineMessage](TypeId.HeapType, "102/3/2/1,2,3/6,7,8") should be (comb3)
  }

  "Serializing and Deserializing DIVIDE messages" should "work" in {
    val div1 = DivideMessage(101, 2, 1, List(IList(List(5, 6, 7)), IList(List(2,5,2))), List(0, 1, 2))
    Serdes.serialize[DivideMessage](TypeId.ListType, div1) should be ("101/2/1/0,1,2/5,6,7;2,5,2")
    Serdes.deserialize[DivideMessage](TypeId.ListType, "101/2/1/0,1,2/5,6,7;2,5,2") should be (div1)

    val div2 = DivideMessage(101, 2, 1, List(IMatrix(List(List(1, 0), List(0, 1))), IMatrix(List(List(2, 0), List(0, 2)))), List(0, 1, 2))
    Serdes.serialize[DivideMessage](TypeId.MatrixType, div2) should be ("101/2/1/0,1,2/1,0,,0,1;2,0,,0,2")
    Serdes.deserialize[DivideMessage](TypeId.MatrixType, "101/2/1/0,1,2/1,0,,0,1;2,0,,0,2") should be (div2)

    val div3 = DivideMessage(102, 3, 2, List(IHeap(List(6, 7, 8)), IHeap(List(1,2,3))), List(1, 2, 3))
    Serdes.serialize[DivideMessage](TypeId.HeapType, div3) should be ("102/3/2/1,2,3/6,7,8;1,2,3")
    Serdes.deserialize[DivideMessage](TypeId.HeapType, "102/3/2/1,2,3/6,7,8;1,2,3") should be (div3)
  }
}
