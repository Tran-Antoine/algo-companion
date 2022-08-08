package ch.epfl.alcmp.data

import ch.epfl.alcmp.net.SimulationMessage.{CombineMessage, DivideMessage}

class SimulationData {

  private var divisionLayers: Map[(Int, Int), InputType] = Map.empty
  private var combineLayers: Map[(Int, Int), InputType] = Map.empty
  private var lengthMap: Map[(Int, Int), Int] = Map.empty
  private var maxDepthValue = 0

  def divisionValueAt(depth: Int, index: Int): InputType = divisionLayers((depth, index))

  def divisionRowAt(depth: Int): List[InputType] =
    rowAt(depth, divisionLayers)

  def combineRowAt(depth: Int): List[InputType] =
    rowAt(depth, combineLayers)

  private def rowAt(depth: Int, map: Map[(Int, Int), InputType]): List[InputType] =
    var base: List[InputType] = Nil
    var i = 0

    while map.contains((depth, i)) do {
      base = base :+ map((depth, i))
      i += 1
    }
    base

  def combineValueAt(depth: Int, index: Int): InputType = combineLayers((depth, index))

  def addDivisionData(message: DivideMessage): Unit =
    val nOutputs = message.outputs.size
    lengthMap += ((message.depth, message.index), nOutputs)
    for i <- 0 until nOutputs do {
      divisionLayers += ((message.depth, message.index + i), message.outputs(i))
    }

    if (maxDepthValue < message.depth) {
      maxDepthValue = message.depth
    }

  def addCombineData(message: CombineMessage): Unit =
    combineLayers += ((message.depth, message.index), message.output)

  def isEmpty: Boolean = divisionLayers.isEmpty && combineLayers.isEmpty

  def maxDepth: Int = maxDepthValue
  def lengthAt(k: (Int, Int)): Int = lengthMap(k)
}
