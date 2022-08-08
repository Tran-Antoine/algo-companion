package ch.epfl.alcmp.gui
import javafx.scene.control.Button
import scalafx.scene.Scene
import scalafx.scene.layout.Pane

class PathVisualizerTest extends Scene {

  {
    val pane = new Pane()
    pane.setPrefSize(1200, 900)
    getChildren.add(pane)

    val button = Button("Next step")
    getChildren.add(button)


    button.setOnAction(_ => {
      PathVisualizer.visualize(pane, (Position(300, 400), Position(700, 200)), Position.NULL)
    })
  }
}
