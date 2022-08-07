package ch.epfl.alcmp.gui

import javafx.animation.Animation
import javafx.animation.FadeTransition
import javafx.geometry.Pos
import javafx.scene.control.Label
import javafx.scene.layout.{HBox, Pane}
import javafx.scene.paint.Color
import javafx.scene.shape.{Circle, Rectangle}
import javafx.util.Duration

object ListVisualizer {

  private val SPACING = 4
  private val ORIGIN_SHIFT = 10
  private val SIZE = 30

  def visualize(parent: Pane, pos: (Int, Int), list: List[Int]): Pane =

    val pane = Pane()
    pane.getStylesheets.add("css/vis-list.css")

    /*val marker = Circle(5)
    pane.getChildren.add(marker)*/

    var numberIndex = 0

    for (number <- list) {
      val element = Label(number.toString)
      element.setPrefSize(SIZE, SIZE)

      val xShift = (SIZE + SPACING) * (numberIndex - list.size / 2.0) + SPACING/2
      numberIndex += 1

      element.setTranslateX(xShift)
      element.setTranslateY(ORIGIN_SHIFT)

      element.getStyleClass.add("number")
      pane.getChildren.add(element)
    }

    parent.getChildren.add(pane)
    pane.relocate(pos._1, pos._2)

    val ft: FadeTransition = FadeTransition(Duration.millis(1000), pane)
    ft.setFromValue(0.0)
    ft.setToValue(1.0)
    ft.setCycleCount(1)
    ft.play()

    pane
}
