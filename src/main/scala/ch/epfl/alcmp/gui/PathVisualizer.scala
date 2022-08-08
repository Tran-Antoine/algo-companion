package ch.epfl.alcmp.gui

import javafx.animation.{Animation, Transition}
import javafx.scene.layout.Pane
import javafx.scene.shape.Line
import javafx.util.Duration

object PathVisualizer extends Visualizer[VisualizableLine] {

  override def visualize(parent: Pane, line: VisualizableLine, pos: Position): Animation =
    val lineObj = Line(line.start.x, line.start.y, line.start.x, line.start.y)
    lineObj.setStrokeWidth(2.5)

    parent.getChildren.add(lineObj)

    val animation = new Transition() {
      {
        setCycleDuration(Duration.millis(1000))
      }
      override def interpolate(frac: Double): Unit =
        val vector = (line.end.x - line.start.x, line.end.y - line.start.y)
        val forward = (frac * vector._1, frac * vector._2)

        lineObj.setEndX(line.start.x + forward._1)
        lineObj.setEndY(line.start.y + forward._2)
    }

    animation.play()
    animation
}
