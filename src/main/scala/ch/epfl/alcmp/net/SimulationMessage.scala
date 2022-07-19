package ch.epfl.alcmp.net

import ch.epfl.alcmp.data.{InputType, TypeId}

sealed trait SimulationMessage {
  def id: Int
}

object SimulationMessage {

  case class RegisterMessage(id: Int) extends SimulationMessage
  case class CombineMessage(id: Int, depth: Int, index: Int, output: InputType, highlights: List[Int]) extends SimulationMessage
  case class DivideMessage(id: Int, depth: Int, index: Int, outputs: List[InputType], highlights: List[Int]) extends SimulationMessage
  case class DoneMessage(id: Int) extends SimulationMessage
}

