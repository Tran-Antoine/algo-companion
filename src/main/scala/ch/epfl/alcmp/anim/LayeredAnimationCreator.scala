package ch.epfl.alcmp.anim

import ch.epfl.alcmp.data.{InputType, SimulationData}
import ch.epfl.alcmp.gui.{CompositeVisualizer, ListVisualizer, PathVisualizer, Position, ScalaFXMain, Visualizable, VisualizableLine, VisualizableList, Visualizer}
import javafx.animation.{Animation, ParallelTransition}
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

      var orderedAnimations: List[Animation] = List(
        Visualizer.convert(data.divisionValueAt(0, 0))(pane, translatePosition(0,0)))
      
      val queue = new mutable.ArrayDeque[(Int, Int)]()

      queue.addOne(0, 0)

      while queue.nonEmpty do {

        val head = queue.removeHead()
        val children = getChildren(head)

        val parentPosition = translatePosition(head)

        val lineAnimations = children
          .map(translatePosition)
          .map(pos => PathVisualizer.visualize(pane, VisualizableLine(parentPosition, pos), Position.NULL))

        val childrenAnimations: List[Animation] = children
          .map((d, i) => (translatePosition(d, i), data.divisionValueAt(d, i)))
          .map((pos, input) => Visualizer.convert(input)(pane, pos))

        orderedAnimations = orderedAnimations ++ List(ParallelTransition(lineAnimations), ParallelTransition(childrenAnimations))
        queue.addAll(children)
      }
      orderedAnimations

    def translatePosition(pos: (Int, Int)): Position =
      val depth = pos._1
      val index = pos._2
      val elementsCount = data.divisionRowAt(depth).size
      Position((index * ScalaFXMain.WIDTH / elementsCount).intValue, (depth + 1) * 100)

    computeAnimations
}

