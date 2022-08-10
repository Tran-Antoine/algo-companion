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
      val skips = index
      var subLayerIndex = 0

      if depth >= data.maxDepth then Nil
      else
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

        if (children.nonEmpty) {
          val parentPosition = translatePosition(head).addY(30)

          val lineAnimations = children
            .map(translatePosition)
            .map(_.addY(-30))
            .map(pos => PathVisualizer.visualize(pane, VisualizableLine(parentPosition, pos), Position.NULL))

          val childrenAnimations: List[Animation] = children
            .map((d, i) => (translatePosition(d, i), data.divisionValueAt(d, i)))
            .map((pos, input) => Visualizer.convert(input)(pane, pos))

          orderedAnimations = orderedAnimations ++ List(parallel(lineAnimations), parallel(childrenAnimations))
          queue.addAll(children)
        }
      }
      orderedAnimations

    def translatePosition(pos: (Int, Int)): Position =
      val depth = pos._1
      val index = pos._2
      val elementsCount = data.divisionRowAt(depth).size

      val xFactor = Math.min((ScalaFXMain.WIDTH * 0.9) / data.maxDepth, 400) // i knew i had the x factor

      val currentLineWidth = depth * xFactor

      val startLine = (ScalaFXMain.WIDTH - currentLineWidth) / 2
      val endLine   = (ScalaFXMain.WIDTH + currentLineWidth) / 2



      val spacing = if elementsCount == 1 then 0 else (endLine - startLine) / (elementsCount - 1)

      val posX = startLine + index * spacing

      val yFactor = (ScalaFXMain.HEIGHT * 0.4) / (data.maxDepth + 1)
      Position(posX.intValue, 50 + (depth * yFactor).intValue)

    computeAnimations


  private def parallel(list: List[Animation]): ParallelTransition =
    val transition = ParallelTransition()
    for (elem <- list) {
      transition.getChildren.add(elem)
    }
    transition
}

