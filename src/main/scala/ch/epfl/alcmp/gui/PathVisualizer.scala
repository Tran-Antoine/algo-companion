package ch.epfl.alcmp.gui

import javafx.animation.Transition
import javafx.scene.layout.Pane
import javafx.scene.shape.Line
import javafx.util.Duration

object PathVisualizer {

  def visualize(parent: Pane, a: (Int, Int), b: (Int, Int)): Line =
    val line = Line(a._1, a._2, a._1, a._2)
    line.setStrokeWidth(2.5)

    parent.getChildren.add(line)

    val animation = new Transition() {
      {
        setCycleDuration(Duration.millis(1000))
      }
      override def interpolate(frac: Double): Unit =
        val vector = (b._1 - a._1, b._2 - a._2)
        //val finalLength = Math.sqrt((b._2 - a._2)*(b._2 - a._2) + (b._2 - a._2) * (b._2 - a._2))
        val forward = (frac * vector._1, frac * vector._2)

        line.setEndX(a._1 + forward._1)
        line.setEndY(a._2 + forward._2)
    }

    animation.play()
    line
}
