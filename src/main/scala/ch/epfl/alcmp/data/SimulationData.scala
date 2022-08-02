package ch.epfl.alcmp.data

import ch.epfl.alcmp.net.SimulationMessage.{CombineMessage, DivideMessage}

class SimulationData {

  private var divisionLayers: Map[(Int, Int), InputType] = Map.empty
  private var combineLayers: Map[(Int, Int), InputType] = Map.empty

  def divisionValueAt(depth: Int, index: Int): InputType = divisionLayers((depth, index))

  def combineValueAt(depth: Int, index: Int): InputType = combineLayers((depth, index))

  def addDivisionData(message: DivideMessage): Unit =
    val nOutputs = message.outputs.size
    for i <- 0 until nOutputs do {
      divisionLayers += ((message.depth, message.index + i), message.outputs(i))
    }

  def addCombineData(message: CombineMessage): Unit =
    combineLayers += ((message.depth, message.index), message.output)
}
