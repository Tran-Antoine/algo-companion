package ch.epfl.alcmp.gui
import javafx.scene.control.Button
import javafx.scene.shape.Circle
import scalafx.scene.Scene
import scalafx.scene.layout.{HBox, Pane, VBox}

class CompositeVisualizerTest extends Scene {

  {
    val pane = new Pane()
    pane.setPrefSize(1200, 900)
    getChildren.add(pane)

    val button = Button("Next step")
    getChildren.add(button)

    val top = List(1, 2, 3, 4)
    val links: List[VisualizableList] = List(List(1, 2), List(1, 2))

    val topPos = Position(600, 100)
    val linksPos = List(Position(300, 250), Position(900, 250))


    button.setOnAction(_ => {
      CompositeVisualizer.drawWithPath[VisualizableList](using ListVisualizer)(pane, top, links, topPos, linksPos)
    })
  }

}
