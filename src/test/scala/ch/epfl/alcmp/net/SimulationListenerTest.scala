package ch.epfl.alcmp.net
import org.scalatest.flatspec.{AnyFlatSpec, AsyncFlatSpec}
import org.scalatest.*
import matchers.*
import ch.epfl.alcmp.data.{SimulationData, TypeId}
import ch.epfl.alcmp.net.SimulationMessage.{DoneMessage, RegisterMessage}

import java.io.{BufferedOutputStream, BufferedWriter, OutputStream, OutputStreamWriter}
import java.net.{ServerSocket, Socket}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}


class SimulationListenerTest extends AsyncFlatSpec with should.Matchers {

  "Simple Register + Done" should "terminate and give a single result" ignore {

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
}
