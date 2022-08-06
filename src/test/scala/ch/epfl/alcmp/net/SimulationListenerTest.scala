package ch.epfl.alcmp.net
import org.scalatest.flatspec.{AnyFlatSpec, AsyncFlatSpec}
import org.scalatest.*
import matchers.*
import ch.epfl.alcmp.data.{IList, SimulationData, TypeId}
import ch.epfl.alcmp.net.SimulationMessage.{CombineMessage, DivideMessage, DoneMessage, RegisterMessage}

import java.io.{BufferedOutputStream, BufferedWriter, OutputStream, OutputStreamWriter}
import java.net.{ServerSocket, Socket}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}


class SimulationListenerTest extends AsyncFlatSpec with should.Matchers {

  "Simple Register + Done" should "terminate and give a single result" in {

    val server = ServerSocket(5108)

    val receiver = Socket("localhost", 5108)
    val sender = server.accept()

    val task: Future[List[SimulationData]] = new SimulationListener(receiver, TypeId.ListType).run()

    val writer = new BufferedWriter(new OutputStreamWriter(sender.getOutputStream))
    writer.write(s"REGISTER ${Serdes.serialize[RegisterMessage](RegisterMessage(1))}\n")
    writer.write(s"DONE ${Serdes.serialize[DoneMessage](DoneMessage(1))}\n")
    writer.flush()
    writer.close()

    receiver.close()
    server.close()

    task map {
      result =>
        result.size should be (1)
        result.head.isEmpty should be (true)
    }
  }

  "Single simulation" should "produce 1 correct SimulationData object" in {
    val server = ServerSocket(5108)

    val receiver = Socket("localhost", 5108)
    val sender = server.accept()

    val task: Future[List[SimulationData]] = new SimulationListener(receiver, TypeId.ListType).run()

    val writer = new BufferedWriter(new OutputStreamWriter(sender.getOutputStream))
    writer.write(s"REGISTER ${Serdes.serialize[RegisterMessage](RegisterMessage(30))}\n")

    val input =  IList(List(1,2,3,4))
    val (left, right) = (IList(List(1,2)), IList(List(3,4)))
    val (oLeft, oRight) = (IList(List(2)), IList(List(4)))
    val output = IList(List(4))

    writer.write(s"DIVIDE ${Serdes.serialize[DivideMessage](
      TypeId.ListType,
      DivideMessage(30, 0, 0, List(input), Nil))}\n")

    writer.write(s"DIVIDE ${Serdes.serialize[DivideMessage](
      TypeId.ListType,
      DivideMessage(30, 1, 0, List(left), Nil))}\n")

    writer.write(s"DIVIDE ${Serdes.serialize[DivideMessage](
      TypeId.ListType,
      DivideMessage(30, 1, 1, List(right), Nil))}\n")

    writer.write(s"COMBINE ${Serdes.serialize[CombineMessage](
      TypeId.ListType,
      CombineMessage(30, 1, 0, oLeft, Nil))}\n")

    writer.write(s"COMBINE ${Serdes.serialize[CombineMessage](
      TypeId.ListType,
      CombineMessage(30, 1, 1, oRight, Nil))}\n")

    writer.write(s"COMBINE ${Serdes.serialize[CombineMessage](
      TypeId.ListType,
      CombineMessage(30, 0, 0, output, Nil))}\n")


    writer.write(s"DONE ${Serdes.serialize[DoneMessage](DoneMessage(30))}\n")
    writer.flush()
    writer.close()

    receiver.close()
    server.close()

    task map {
      result =>
        result.size should be (1)

        val head = result.head

        head.combineValueAt(0, 0) should be (output)
        head.divisionRowAt(0) should be (List(input))
        head.divisionValueAt(1, 0) should be (left)
        head.divisionValueAt(1, 1) should be (right)
    }
  }

  "Multiple simulation in random order" should "produce multiple correct SimulationData objects" in {
    val server = ServerSocket(5108)

    val receiver = Socket("localhost", 5108)
    val sender = server.accept()

    val task: Future[List[SimulationData]] = new SimulationListener(receiver, TypeId.ListType).run()

    val writer = new BufferedWriter(new OutputStreamWriter(sender.getOutputStream))


    val input1 =  IList(List(1,2,3,4))
    val (left1, right1) = (IList(List(1,2)), IList(List(3,4)))
    val (oLeft1, oRight1) = (IList(List(2)), IList(List(4)))
    val output1 = IList(List(4))

    val input2 =  IList(List(5,6,7,8))
    val (left2, right2) = (IList(List(5,6)), IList(List(7,8)))
    val (oLeft2, oRight2) = (IList(List(6)), IList(List(8)))
    val output2 = IList(List(8))

    writer.write(s"REGISTER ${Serdes.serialize[RegisterMessage](RegisterMessage(30))}\n")


    writer.write(s"DIVIDE ${Serdes.serialize[DivideMessage](
      TypeId.ListType,
      DivideMessage(30, 0, 0, List(input1), Nil))}\n")

    writer.write(s"DIVIDE ${Serdes.serialize[DivideMessage](
      TypeId.ListType,
      DivideMessage(30, 1, 0, List(left1), Nil))}\n")


    writer.write(s"REGISTER ${Serdes.serialize[RegisterMessage](RegisterMessage(31))}\n")


    writer.write(s"DIVIDE ${Serdes.serialize[DivideMessage](
      TypeId.ListType,
      DivideMessage(31, 1, 0, List(left2), Nil))}\n")


    writer.write(s"COMBINE ${Serdes.serialize[CombineMessage](
      TypeId.ListType,
      CombineMessage(30, 1, 0, oLeft1, Nil))}\n")

    writer.write(s"DIVIDE ${Serdes.serialize[DivideMessage](
      TypeId.ListType,
      DivideMessage(31, 0, 0, List(input2), Nil))}\n")

    writer.write(s"DIVIDE ${Serdes.serialize[DivideMessage](
      TypeId.ListType,
      DivideMessage(31, 1, 1, List(right2), Nil))}\n")

    writer.write(s"DIVIDE ${Serdes.serialize[DivideMessage](
      TypeId.ListType,
      DivideMessage(30, 1, 1, List(right1), Nil))}\n")

    writer.write(s"COMBINE ${Serdes.serialize[CombineMessage](
      TypeId.ListType,
      CombineMessage(31, 1, 0, oLeft2, Nil))}\n")

    writer.write(s"COMBINE ${Serdes.serialize[CombineMessage](
      TypeId.ListType,
      CombineMessage(31, 1, 1, oRight2, Nil))}\n")

    writer.write(s"COMBINE ${Serdes.serialize[CombineMessage](
      TypeId.ListType,
      CombineMessage(31, 0, 0, output2, Nil))}\n")

    writer.write(s"DONE ${Serdes.serialize[DoneMessage](DoneMessage(31))}\n")

    writer.write(s"COMBINE ${Serdes.serialize[CombineMessage](
      TypeId.ListType,
      CombineMessage(30, 1, 1, oRight1, Nil))}\n")

    writer.write(s"COMBINE ${Serdes.serialize[CombineMessage](
      TypeId.ListType,
      CombineMessage(30, 0, 0, output1, Nil))}\n")

    writer.write(s"DONE ${Serdes.serialize[DoneMessage](DoneMessage(30))}\n")


    writer.flush()
    writer.close()

    receiver.close()
    server.close()

    task map {
      result =>
        result.size should be (2)

        val r1 = result.head
        val r2 = result.tail.head

        r1.combineValueAt(0, 0) should be (output1)
        r1.divisionRowAt(0) should be (List(input1))
        r1.divisionValueAt(1, 0) should be (left1)
        r1.divisionValueAt(1, 1) should be (right1)

        r2.combineValueAt(0, 0) should be (output2)
        r2.divisionRowAt(0) should be (List(input2))
        r2.divisionValueAt(1, 0) should be (left2)
        r2.divisionValueAt(1, 1) should be (right2)
    }
  }
}
