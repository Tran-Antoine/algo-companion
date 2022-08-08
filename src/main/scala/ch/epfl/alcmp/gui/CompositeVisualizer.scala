package ch.epfl.alcmp.gui

import javafx.animation.{Animation, ParallelTransition, PauseTransition, SequentialTransition, Transition}
import javafx.scene.layout.Pane
import javafx.scene.Node
import javafx.util.Duration

object CompositeVisualizer {

  def drawWithPath[T <: Visualizable](using v1: Visualizer[T])
                                     (pane: Pane, top: T, links: List[T], topPos: Position, linksPos: List[Position]): Animation =


    val anim1 = v1.visualize(pane, top, topPos)

    val shiftedTopPosition = Position(topPos.x, topPos.y + 60)
    val anim2 = ParallelTransition()
    for (pos <- linksPos) {
      anim2.getChildren.add(PathVisualizer.visualize(pane, (shiftedTopPosition, pos), topPos))
    }

    val anim3 = SequentialTransition()
    for ((link, linkPos) <- links.zip(linksPos)) {
      anim3.getChildren.add(v1.visualize(pane, link, linkPos))
    }

    val fullAnimation = SequentialTransition(anim1, anim2, anim3)
    fullAnimation
}
