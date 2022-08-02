package ch.epfl.alcmp.net

import ch.epfl.alcmp.data.{IBinaryTree, IHeap, IList, IMatrix, TypeId}
import ch.epfl.alcmp.net.SimulationMessage.{CombineMessage, DivideMessage, DoneMessage, RegisterMessage}

import java.nio.charset.StandardCharsets
import java.util.Base64

sealed trait Serde[T] {

  def serialize(obj: T): String
  def deserialize(data: String): T
}

object Serde {

  given Serde[Int] with
    override def serialize(obj: Int): String = String.valueOf(obj)
    override def deserialize(str: String): Int = Integer.parseInt(str)

  given Serde[String] with
    override def serialize(obj: String): String = Base64.getEncoder.encodeToString(obj.getBytes(StandardCharsets.UTF_8))
    override def deserialize(data: String): String = new String(Base64.getDecoder.decode(data), StandardCharsets.UTF_8)

  //given Serde[List[]] with


  given Serde[RegisterMessage] with
    override def serialize(obj: RegisterMessage): String = Serdes.serialize[Int](obj.id)
    override def deserialize(data: String): RegisterMessage = RegisterMessage(Serdes.deserialize[Int](data))


  given Serde[DoneMessage] with
    override def serialize(obj: DoneMessage): String = Serdes.serialize[Int](obj.id)
    override def deserialize(data: String): DoneMessage = DoneMessage(Serdes.deserialize[Int](data))

  given (TypeId => Serde[DivideMessage]) with
    override def apply(t: TypeId): Serde[DivideMessage] = new Serde[DivideMessage]:
      override def serialize(obj: DivideMessage): String = t match
        case TypeId.ListType => ???
        case TypeId.MatrixType => ???
        case TypeId.HeapType => ???
        case TypeId.BinaryTreeType => ???
      override def deserialize(data: String): DivideMessage = t match
        case TypeId.ListType => ???
        case TypeId.MatrixType => ???
        case TypeId.HeapType => ???
        case TypeId.BinaryTreeType => ???

  given (TypeId => Serde[CombineMessage]) with
    override def apply(t: TypeId): Serde[CombineMessage] = new Serde[CombineMessage]:
      override def serialize(obj: CombineMessage): String = obj.output match {
        //TODO change
        case IList(list) => String.valueOf(obj.id) + '/' + String.valueOf(obj.depth) + '/' +
          String.valueOf(obj.index) + '/' + list.mkString(",") + '/' + obj.highlights.mkString(",")
        case IMatrix(rows) => ???
        case IHeap(list) => ???
        case IBinaryTree(root) => ???
      }
      override def deserialize(data: String): CombineMessage = t match
        case TypeId.ListType =>
          val arr = data.split("/")
          val ilist = arr(3).split(",").map(s => s.toInt)
          val highlights = arr(4).split(",").map(s => s.toInt)
          CombineMessage(arr(0).toInt, arr(1).toInt, arr(2).toInt, IList(ilist.toList), highlights.toList)
        case TypeId.MatrixType => ???
        case TypeId.HeapType => ???
        case TypeId.BinaryTreeType => ???

}
