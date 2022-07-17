package ch.epfl.alcmp.net

import ch.epfl.alcmp.data.InputType

sealed trait SimulationMessage {
  def id: Int
}

object SimulationMessage {

  case class RegisterMessage(id: Int) extends SimulationMessage
  case class CombineMessage[T <: InputType](id: Int, depth: Int, index: Int, output: T, highlights: List[Int]) extends SimulationMessage
  case class DivideMessage[T <: InputType](id: Int, depth: Int, index: Int, outputs: List[T], highlights: List[Int]) extends SimulationMessage
  case class DoneMessage(id: Int) extends SimulationMessage
}

