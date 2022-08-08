package ch.epfl.alcmp.gui

import javafx.animation.{Animation, Transition}
import javafx.scene.layout.Pane
import javafx.scene.Node
import javafx.util.Duration

object CompositeVisualizer {

  def drawWithPath[T <: Visualizable](using v1: Visualizer[T])
                                     (pane: Pane, top: T, links: List[T], topPos: Position, linksPos: List[Position]): Animation =

    // TODO: use CompositeTransition + ParallelTransition instead
    val animation: Transition = new Transition() {
      var started = false
      var reachedThird = false
      var reachedSecondThird = false
      val shiftedTopPosition = Position(topPos.x, topPos.y + 60)

      {
        setCycleDuration(Duration.millis(3000))
      }
      override def interpolate(v: Double): Unit =
        if v >= 0 && !started then
          started = true
          v1.visualize(pane, top, topPos)
        else if v >= 0.33 && !reachedThird then
          reachedThird = true
          for (pos <- linksPos) {
            PathVisualizer.visualize(pane, (shiftedTopPosition, pos), topPos)
          }
        else if v >= 0.66 && !reachedSecondThird then
          reachedSecondThird = true
          for ((link, linkPos) <- links.zip(linksPos)) {
            v1.visualize(pane, link, linkPos)
          }
    }

    animation.play()
    animation
}
