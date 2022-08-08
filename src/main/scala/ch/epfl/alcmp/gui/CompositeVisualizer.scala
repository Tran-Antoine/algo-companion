package ch.epfl.alcmp.gui

import javafx.animation.{Animation, ParallelTransition, PauseTransition, SequentialTransition, Transition}
import javafx.scene.layout.Pane
import javafx.scene.Node
import javafx.util.Duration

object CompositeVisualizer {

  def drawWithPath[T <: Visualizable](using v1: Visualizer[T])
                                     (pane: Pane, top: T, links: List[T], topPos: Position, linksPos: List[Position]): Animation =


    val anim1 = v1.visualize(pane, top, topPos)

    val shiftedTopPosition = Position(topPos.x, topPos.y + shiftFor(top))
    val anim2 = ParallelTransition()
    for (pos <- linksPos) {
      anim2.getChildren.add(Visualizer.visualize[VisualizableLine](pane, (shiftedTopPosition, pos), topPos))
    }

    val anim3 = SequentialTransition()
    for ((link, linkPos) <- links.zip(linksPos)) {
      anim3.getChildren.add(v1.visualize(pane, link, linkPos))
    }

    val fullAnimation = SequentialTransition(anim1, anim2, anim3)
    fullAnimation

  private def shiftFor(v: Visualizable): Int = v match {
    case VisualizableList(_) => 60
    case VisualizableLine(_, _) => 0
    case _ => 0
  }
}
