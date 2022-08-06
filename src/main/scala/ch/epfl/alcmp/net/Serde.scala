package ch.epfl.alcmp.net

import ch.epfl.alcmp.data.{IBinaryTree, IHeap, IList, IMatrix, InputType, TypeId}
import ch.epfl.alcmp.net.SimulationMessage.{CombineMessage, DivideMessage, DoneMessage, RegisterMessage}

import java.nio.charset.StandardCharsets
import java.util.{Base64, StringJoiner}
import scala.reflect.ClassTag

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

  def listOf[T](using Serde[T])(sep: String): Serde[List[T]] = new Serde[List[T]]() {
    override def serialize(arg: List[T]): String = arg.map(elem => Serdes.serialize[T](elem)).mkString(sep)
    override def deserialize(data: String): List[T] = data.split(sep).toList.map(elem => Serdes.deserialize[T](elem))
  }

  given (TypeId => Serde[DivideMessage]) with
    override def apply(t: TypeId): Serde[DivideMessage] = new Serde[DivideMessage]:
      override def serialize(obj: DivideMessage): String =
        val b1 = StringJoiner("/")
                .add(obj.id.toString)
                .add(obj.depth.toString)
                .add(obj.index.toString)
                .add(listOf[Int](",").serialize(obj.highlights))

        val b2 = StringJoiner(";")
        for ( output <- obj.outputs) {
          output match
            case IList(list) => b2.add(listOf[Int](",").serialize(list))
            case IMatrix(rows) =>
              val b3 = StringJoiner(",,")
              rows.foreach(list => b3.add(listOf[Int](",").serialize(list)))
              b2.add(b3.toString)
            case IHeap(list) => b2.add(listOf[Int](",").serialize(list))
            case IBinaryTree(root) => ???
        }
        b1.add(b2.toString)
        b1.toString

      override def deserialize(data: String): DivideMessage =
        val arr = data.split("/")
        val (id, depth, index) = (Serdes.deserialize[Int](arr(0)), Serdes.deserialize[Int](arr(1)), Serdes.deserialize[Int](arr(2)))
        val highlights = listOf[Int](",").deserialize(arr(3))
        t match
        case TypeId.ListType =>
          val outputs = arr(4).split(";").toList.map(list => IList(listOf[Int](",").deserialize(list)))
          DivideMessage(id, depth, index, outputs, highlights)
        case TypeId.MatrixType =>
          val outputs = arr(4).split(";").map(lists =>
              IMatrix(lists.split(",,").map(list => listOf[Int](",").deserialize(list).toList).toList)
            ).toList
          DivideMessage(id, depth, index, outputs, highlights)
        case TypeId.HeapType =>
          val outputs = arr(4).split(";").toList.map(list => IHeap(listOf[Int](",").deserialize(list)))
          DivideMessage(id, depth, index, outputs, highlights)
        case TypeId.BinaryTreeType => ???

  given (TypeId => Serde[CombineMessage]) with
    override def apply(t: TypeId): Serde[CombineMessage] = new Serde[CombineMessage]:
      override def serialize(obj: CombineMessage): String =
        val b1 = StringJoiner("/")
          .add(obj.id.toString)
          .add(obj.depth.toString)
          .add(obj.index.toString)
          .add(listOf[Int](",").serialize(obj.highlights))
        obj.output match {
          case IList(list) => b1.add(list.mkString(","))
          case IMatrix(rows) =>
            val b2 = StringJoiner(",,")
            rows.foreach(list => b2.add(listOf[Int](",").serialize(list)))
            b1.add(b2.toString)
          case IHeap(list) => b1.add(list.mkString(","))
          case IBinaryTree(root) => ???
        }
        b1.toString
      override def deserialize(data: String): CombineMessage =
        val arr = data.split("/")
        val (id, depth, index) = (Serdes.deserialize[Int](arr(0)), Serdes.deserialize[Int](arr(1)), Serdes.deserialize[Int](arr(2)))
        val highlights = listOf[Int](",").deserialize(arr(3))
        t match
        case TypeId.ListType =>
          val output = listOf[Int](",").deserialize(arr(4))
          CombineMessage(id, depth, index, IList(output), highlights)
        case TypeId.MatrixType =>
          val output = arr(4).split(",,").toList.map(s => listOf[Int](",").deserialize(s))
          CombineMessage(id, depth, index, IMatrix(output), highlights)
        case TypeId.HeapType =>
          val output = listOf[Int](",").deserialize(arr(3))
          CombineMessage(id, depth, index, IHeap(output), highlights)
        case TypeId.BinaryTreeType => ???
}
