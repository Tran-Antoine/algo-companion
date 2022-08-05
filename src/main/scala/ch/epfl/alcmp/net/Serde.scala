package ch.epfl.alcmp.net

import ch.epfl.alcmp.data.{IBinaryTree, IHeap, IList, IMatrix, InputType, TypeId}
import ch.epfl.alcmp.net.SimulationMessage.{CombineMessage, DivideMessage, DoneMessage, RegisterMessage}

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

  given Serde[DoneMessage] with
    override def serialize(obj: DoneMessage): String = Serdes.serialize[Int](obj.id)
    override def deserialize(data: String): DoneMessage = DoneMessage(Serdes.deserialize[Int](data))

  given (TypeId => Serde[DivideMessage]) with
    override def apply(t: TypeId): Serde[DivideMessage] = new Serde[DivideMessage]:
      override def serialize(obj: DivideMessage): String =
        val s = List(obj.id, obj.depth, obj.index, obj.highlights.mkString(",")).mkString("/") + "/"
        val b1 = StringJoiner(";")
        for ( output <- obj.outputs) {
          output match
            case IList(list) => b1.add(list.mkString(","))
            case IMatrix(rows) =>
              val b2 = StringJoiner(",,")
              rows.foreach(list => b2.add(list.mkString(",")))
              b1.add(b2.toString)
            case IHeap(list) => b1.add(list.mkString(","))
            case IBinaryTree(root) => ???
        }
        s + b1.toString

      override def deserialize(data: String): DivideMessage =
        val arr = data.split("/")
        val (id, depth, index) = (arr(0).toInt, arr(1).toInt, arr(2).toInt)
        val highlights = arr(3).split(",").map(s => s.toInt).toList
        t match
        case TypeId.ListType =>
          val outputs = arr(4).split(";")
                        .map(list => IList(list.split(",").map(s2 => s2.toInt).toList)).toList
          DivideMessage(id, depth, index, outputs, highlights)
        case TypeId.MatrixType =>
          val outputs = arr(4).split(";")
            .map(lists => IMatrix(lists.split(",,")
            .map(_.split(",").map(number => number.toInt).toList).toList)).toList
          DivideMessage(id, depth, index, outputs, highlights)
        case TypeId.HeapType =>
          val outputs = arr(4).split(";")
            .map(list => IHeap(list.split(",").map(s2 => s2.toInt).toList)).toList
          DivideMessage(id, depth, index, outputs, highlights)
        case TypeId.BinaryTreeType => ???

  given (TypeId => Serde[CombineMessage]) with
    override def apply(t: TypeId): Serde[CombineMessage] = new Serde[CombineMessage]:
      override def serialize(obj: CombineMessage): String =
        val commonPart = List(obj.id, obj.depth, obj.index, obj.highlights.mkString(",")).mkString("/")
        obj.output match {
          case IList(list) => commonPart + "/" + list.mkString(",")
          case IMatrix(rows) =>
            val b = StringJoiner(",,")
            rows.foreach(list => b.add(list.mkString(",")))
            commonPart + "/" + b.toString
          case IHeap(list) => commonPart + '/' + list.mkString(",")
          case IBinaryTree(root) => ???
        }
      override def deserialize(data: String): CombineMessage =
        val arr = data.split("/")
        val (id, depth, index) = (arr(0).toInt, arr(1).toInt, arr(2).toInt)
        val highlights = arr(3).split(",").map(s => s.toInt).toList
        t match
        case TypeId.ListType =>
          val output = arr(4).split(",").map(s => s.toInt).toList
          CombineMessage(id, depth, index, IList(output), highlights)
        case TypeId.MatrixType =>
          val output = arr(4).split(",,").map(s => s.split(",").map(s => s.toInt).toList).toList
          CombineMessage(id, depth, index, IMatrix(output), highlights)
        case TypeId.HeapType =>
          val output = arr(4).split(",").map(s => s.toInt).toList
          CombineMessage(id, depth, index, IHeap(output), highlights)
        case TypeId.BinaryTreeType => ???
}
