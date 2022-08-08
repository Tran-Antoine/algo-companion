package ch.epfl.alcmp.anim

import ch.epfl.alcmp.data.{InputType, SimulationData}
import javafx.animation.Animation

import scala.collection.mutable

class LayeredAnimationCreator(data: SimulationData) {

  def computeSteps: List[Animation] =

    def getChildren(pos: (Int, Int)): List[(Int, Int)] =
      val depth = pos._1
      val index = pos._2
      val skips = index - 1
      var subLayerIndex = 0
      for (_ <- 0 until skips) {
        subLayerIndex += data.lengthAt(depth + 1, subLayerIndex)
      }
      val childrenCount = data.lengthAt(depth + 1, subLayerIndex)

      (0 until childrenCount).map(i => (depth + 1, subLayerIndex + i)).toList

    def computeGroups: List[List[InputType]] =
      var orderedGroups: List[List[InputType]] = List(List(data.divisionValueAt(0, 0)))
      val queue = new mutable.ArrayDeque[(Int, Int)]()

      queue.addOne(List((0, 0)))

      while queue.nonEmpty do {
        val head = queue.removeHead()
        val children = getChildren(head)

        orderedGroups = orderedGroups :+ children.map((d, i) => data.divisionValueAt(d, i))
        queue.addAll(children)
      }
      orderedGroups

    def computeAnimationList(groups: List[List[InputType]]): List[Animation] = ???

    val groups = computeGroups
    computeAnimationList(groups)
}