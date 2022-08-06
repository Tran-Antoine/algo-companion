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
    writer.write(s"REGISTER ${Serdes.serialize[RegisterMessage](RegisterMessage(1))}\n")

    val input =  IList(List(1,2,3,4))
    val (left, right) = (IList(List(1,2)), IList(List(3,4)))
    val (oLeft, oRight) = (IList(List(2)), IList(List(4)))
    val result = IList(List(4))

    writer.write(s"DIVIDE ${Serdes.serialize[DivideMessage](
      TypeId.ListType,
      DivideMessage(0, 0, 0, List(input), Nil))}\n")

    writer.write(s"DIVIDE ${Serdes.serialize[DivideMessage](
      TypeId.ListType,
      DivideMessage(0, 1, 0, List(left), Nil))}\n")

    writer.write(s"DIVIDE ${Serdes.serialize[DivideMessage](
      TypeId.ListType,
      DivideMessage(0, 1, 1, List(right), Nil))}\n")

    writer.write(s"COMBINE ${Serdes.serialize[CombineMessage](
      TypeId.ListType,
      CombineMessage(0, 1, 0, oLeft, Nil))}\n")

    writer.write(s"COMBINE ${Serdes.serialize[CombineMessage](
      TypeId.ListType,
      CombineMessage(0, 1, 1, oRight, Nil))}\n")

    writer.write(s"COMBINE ${Serdes.serialize[CombineMessage](
      TypeId.ListType,
      CombineMessage(0, 0, 0, result, Nil))}\n")


    writer.write(s"DONE ${Serdes.serialize[DoneMessage](DoneMessage(1))}\n")
    writer.flush()
    writer.close()

    task map {
      result =>
        result.size should be (1)

        val head = result.head

        head.combineValueAt(0, 0) should be (IList(List(4)))
        head.divisionRowAt(0) should be (List(IList(List(1,2,3,4))))
        head.divisionValueAt(1, 1) should be (right)
    }
  }
}
