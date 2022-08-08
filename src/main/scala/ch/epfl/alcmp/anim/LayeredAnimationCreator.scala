package ch.epfl.alcmp.anim

import ch.epfl.alcmp.data.{InputType, SimulationData}
import ch.epfl.alcmp.gui.{CompositeVisualizer, ListVisualizer, Position, Visualizable, VisualizableList, Visualizer}
import javafx.animation.Animation
import javafx.scene.layout.Pane

import scala.collection.mutable

class LayeredAnimationCreator(data: SimulationData) {

  def computeSteps(pane: Pane): List[Animation] =

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

    def computeAnimations: List[Animation] =

      // TODO: fix this type problem
      var orderedGroups: List[Animation] = List(ListVisualizer.visualize(pane, data.divisionValueAt(0, 0).asInstanceOf[VisualizableList], translatePosition(0, 0)))
      val queue = new mutable.ArrayDeque[(Int, Int)]()

      queue.addOne(0, 0)

      while queue.nonEmpty do {
        val head = queue.removeHead()
        val headObject = data.divisionValueAt(head._1, head._2).asInstanceOf[VisualizableList]
        val children = getChildren(head)

        val childrenObjects: List[VisualizableList] = children.map((d, i) => data.divisionValueAt(d, i).asInstanceOf[VisualizableList])
        val childrenPositions = children.map((d, i) => translatePosition(d, i))
        val parentPosition = translatePosition(head)

        val animation = CompositeVisualizer.drawWithPath[VisualizableList](pane, headObject, childrenObjects, parentPosition, childrenPositions)


        orderedGroups = orderedGroups :+ animation
        queue.addAll(children)
      }
      orderedGroups

    def translatePosition(pos: (Int, Int)): Position =
      ???

    computeAnimations
}

