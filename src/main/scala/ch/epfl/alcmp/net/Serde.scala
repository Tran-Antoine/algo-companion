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

  given Serde[List[Int]] with
    override def serialize(obj: List[Int]): String = listOf[Int](",").serialize(obj)
    override def deserialize(data: String): List[Int] = listOf[Int](",").deserialize(data)

  given Serde[RegisterMessage] with
    override def serialize(obj: RegisterMessage): String = Serdes.serialize[Int](obj.id)
    override def deserialize(data: String): RegisterMessage = RegisterMessage(Serdes.deserialize[Int](data))

  given Serde[DoneMessage] with
    override def serialize(obj: DoneMessage): String = Serdes.serialize[Int](obj.id)
    override def deserialize(data: String): DoneMessage = DoneMessage(Serdes.deserialize[Int](data))

  def listOf[T](using Serde[T])(sep: String): Serde[List[T]] = new Serde[List[T]]():
    override def serialize(arg: List[T]): String = arg.map(elem => Serdes.serialize[T](elem)).mkString(sep)
    override def deserialize(data: String): List[T] =
      if data.isEmpty then Nil
      else data.split(sep).toList.map(elem => Serdes.deserialize[T](elem))

  given Serde[IList] with
    override def serialize(obj: IList): String = Serdes.serialize[List[Int]](obj.list)
    override def deserialize(data: String): IList = IList(Serdes.deserialize[List[Int]](data))

  given Serde[IHeap] with
    override def serialize(obj: IHeap): String = Serdes.serialize[List[Int]](obj.list)
    override def deserialize(data: String): IHeap = IHeap(Serdes.deserialize[List[Int]](data))

  given Serde[IMatrix] with
    override def serialize(obj: IMatrix): String = listOf[List[Int]](",,").serialize(obj.rows)
    override def deserialize(data: String): IMatrix = IMatrix(listOf[List[Int]](",,").deserialize(data))

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
            case ilist: IList => b2.add(Serdes.serialize[IList](ilist))
            case imatrix: IMatrix => b2.add(Serdes.serialize[IMatrix](imatrix))
            case iheap: IHeap => b2.add(Serdes.serialize[IHeap](iheap))
            case ibinary: IBinaryTree => ???
        }
        b1.add(b2.toString)
        b1.toString

      override def deserialize(data: String): DivideMessage =
        val arr = data.split("/")
        val (id, depth, index) = (Serdes.deserialize[Int](arr(0)), Serdes.deserialize[Int](arr(1)), Serdes.deserialize[Int](arr(2)))
        val highlights = listOf[Int](",").deserialize(arr(3))
        val outputs = t match
          case TypeId.ListType => listOf[IList](";").deserialize(arr(4))
          case TypeId.MatrixType => listOf[IMatrix](";").deserialize(arr(4))
          case TypeId.HeapType => listOf[IHeap](";").deserialize(arr(4))
          case TypeId.BinaryTreeType => ???
        DivideMessage(id, depth, index, outputs, highlights)

  given (TypeId => Serde[CombineMessage]) with
    override def apply(t: TypeId): Serde[CombineMessage] = new Serde[CombineMessage]:
      override def serialize(obj: CombineMessage): String =
        val b1 = StringJoiner("/")
          .add(obj.id.toString)
          .add(obj.depth.toString)
          .add(obj.index.toString)
          .add(listOf[Int](",").serialize(obj.highlights))
        obj.output match
          case ilist: IList => b1.add(Serdes.serialize[IList](ilist))
          case imatrix: IMatrix => b1.add(Serdes.serialize[IMatrix](imatrix))
          case iheap: IHeap => b1.add(Serdes.serialize[IHeap](iheap))
          case ibinary: IBinaryTree => ???

        b1.toString
      override def deserialize(data: String): CombineMessage =
        val arr = data.split("/")
        val (id, depth, index) = (Serdes.deserialize[Int](arr(0)), Serdes.deserialize[Int](arr(1)), Serdes.deserialize[Int](arr(2)))
        val highlights = listOf[Int](",").deserialize(arr(3))
        val output = t match
          case TypeId.ListType => Serdes.deserialize[IList](arr(4))
          case TypeId.MatrixType => Serdes.deserialize[IMatrix](arr(4))
          case TypeId.HeapType => Serdes.deserialize[IHeap](arr(4))
          case TypeId.BinaryTreeType => ???
        CombineMessage(id, depth, index, output, highlights)
}
