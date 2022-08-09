package ch.epfl.alcmp.gui

import javafx.animation.{Animation, ParallelTransition, PauseTransition, SequentialTransition, Transition}
import javafx.scene.layout.Pane
import javafx.scene.Node
import javafx.util.Duration

object CompositeVisualizer {

  def drawWithPathWithOriginal[T <: Visualizable](using v1: Visualizer[T])
                                                           (pane: Pane, top: T, links: List[T], topPos: Position, linksPos: List[Position]): Animation =


    val anim1 = v1.visualize(pane, top, topPos)
    SequentialTransition(anim1, drawWithPath[T](pane, links, topPos, linksPos))

  def drawWithPath[T <: Visualizable](using v1: Visualizer[T])
                                     (pane: Pane, links: List[T], topPos: Position, linksPos: List[Position]): Animation =

    // TODO: have proper shift depending on type
    val shiftedTopPosition = Position(topPos.x, topPos.y + 60)
    val anim1 = ParallelTransition()
    for (pos <- linksPos) {
      anim1.getChildren.add(Visualizer.visualize[VisualizableLine](pane, (shiftedTopPosition, pos), topPos))
    }

    val anim2 = SequentialTransition()
    for ((link, linkPos) <- links.zip(linksPos)) {
      anim2.getChildren.add(v1.visualize(pane, link, linkPos))
    }

    val fullAnimation = SequentialTransition(anim1, anim2)
    fullAnimation

  private def shiftFor(v: Visualizable): Int = v match {
    case VisualizableList(_) => 60
    case VisualizableLine(_, _) => 0
    case _ => 0
  }
}
