package ch.epfl.alcmp.net

import ch.epfl.alcmp.data.{IBinaryTree, IHeap, IList, IMatrix, TypeId}
import ch.epfl.alcmp.net.SimulationMessage.{CombineMessage, DivideMessage, DoneMessage, RegisterMessage}
import jdk.internal.joptsimple.internal.Strings

import java.nio.charset.StandardCharsets
import java.util.{Base64, StringJoiner}

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


  given Serde[RegisterMessage] with
    override def serialize(obj: RegisterMessage): String = Serdes.serialize[Int](obj.id)
    override def deserialize(data: String): RegisterMessage = RegisterMessage(Serdes.deserialize[Int](data))

  //given Serde[List[String]]

  given Serde[DoneMessage] with
    override def serialize(obj: DoneMessage): String = Serdes.serialize[Int](obj.id)
    override def deserialize(data: String): DoneMessage = DoneMessage(Serdes.deserialize[Int](data))

  given (TypeId => Serde[DivideMessage]) with
    override def apply(t: TypeId): Serde[DivideMessage] = new Serde[DivideMessage]:
      override def serialize(obj: DivideMessage): String =
        val commonPart = List(obj.id, obj.depth, obj.index, obj.highlights.mkString("(", ",", ")")).mkString("/")
        t match
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
      override def serialize(obj: CombineMessage): String =
        val commonPart = List(obj.id, obj.depth, obj.index, obj.highlights.mkString("(", ",", ")")).mkString("/")
        obj.output match {
        case IList(list) => commonPart + '/' + list.mkString("(", ",", ")")
        case IMatrix(rows) =>
          val b = new StringJoiner(",", "(", ")")
          rows.foreach(list => b.add(list.mkString("(", ",", ")")))
          commonPart + '/' + b.toString
        case IHeap(list) => commonPart + '/' + list.mkString("(", ",", ")")
        case IBinaryTree(root) => ???
      }
      override def deserialize(data: String): CombineMessage =
        val arr = data.split("/")
        val highlights = arr(3).substring(1, arr(3).length - 1).split(",").map(s => s.toInt)
        t match
        case TypeId.ListType =>
          val output = arr(4).substring(1, arr(4).length - 1).split(",").map(s => s.toInt)
          CombineMessage(arr(0).toInt, arr(1).toInt, arr(2).toInt, IList(output.toList), highlights.toList)
        case TypeId.MatrixType =>
          val output = arr(4).substring(2, arr(4).length - 2).split("\\),\\(")
            .map(s => s.split(",").map(s => s.toInt).toList)
          print(output)
          CombineMessage(arr(0).toInt, arr(1).toInt, arr(2).toInt, IMatrix(output.toList), highlights.toList)
        case TypeId.HeapType =>
          val iheap = arr(4).substring(1, arr(4).length - 1).split(",").map(s => s.toInt)
          CombineMessage(arr(0).toInt, arr(1).toInt, arr(2).toInt, IHeap(iheap.toList), highlights.toList)
        case TypeId.BinaryTreeType => ???
}
