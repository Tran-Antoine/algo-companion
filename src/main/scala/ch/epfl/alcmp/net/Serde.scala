package ch.epfl.alcmp.net

import ch.epfl.alcmp.data.InputType
import ch.epfl.alcmp.data.InputType.{IBinaryTree, IHeap, IList, IMatrix}
import ch.epfl.alcmp.net.SimulationMessage.{DivideMessage, RegisterMessage}

import java.nio.charset.StandardCharsets
import java.util.Base64

trait Serde[T] {

  def serialize(obj: T): String
  def deserialize(data: String): T
}

object Serde {

  val NUMBER: Serde[Int] = new Serde[Int] {
    override def serialize(obj: Int): String = String.valueOf(obj)
    override def deserialize(data: String): Int = Integer.parseInt(data)
  }

  val TEXT: Serde[String] = new Serde[String] {
    override def serialize(obj: String): String = Base64.getEncoder.encodeToString(obj.getBytes(StandardCharsets.UTF_8))
    override def deserialize(data: String): String = new String(Base64.getDecoder.decode(data), StandardCharsets.UTF_8)
  }

  val REGISTER_MESSAGE: Serde[RegisterMessage] = new Serde[RegisterMessage] {
    override def serialize(obj: RegisterMessage): String = ???
    override def deserialize(data: String): RegisterMessage = ???
  }

  val DIVIDE_LIST_MESSAGE: Serde[DivideMessage[IList]] = new Serde[DivideMessage[IList]] {
    override def serialize(obj: DivideMessage[IList]): String = ???
    override def deserialize(data: String): DivideMessage[IList] = ???
  }

  val DIVIDE_MATRIX_MESSAGE: Serde[DivideMessage[IMatrix]] = new Serde[DivideMessage[IMatrix]] {
    override def serialize(obj: DivideMessage[IMatrix]): String = ???
    override def deserialize(data: String): DivideMessage[IMatrix] = ???
  }

  val DIVIDE_HEAP_MESSAGE: Serde[DivideMessage[IHeap]] = new Serde[DivideMessage[IHeap]] {
    override def serialize(obj: DivideMessage[IHeap]): String = ???
    override def deserialize(data: String): DivideMessage[IHeap] = ???
  }

  val DIVIDE_BINARY_TREE_MESSAGE: Serde[DivideMessage[IBinaryTree]] = new Serde[DivideMessage[IBinaryTree]] {
    override def serialize(obj: DivideMessage[IBinaryTree]): String = ???
    override def deserialize(data: String): DivideMessage[IBinaryTree] = ???
  }
}