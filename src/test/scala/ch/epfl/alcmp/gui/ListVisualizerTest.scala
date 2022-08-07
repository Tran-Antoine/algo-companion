package ch.epfl.alcmp.gui
import javafx.scene.control.Button
import javafx.scene.shape.Circle
import scalafx.scene.Scene
import scalafx.scene.layout.{HBox, Pane, VBox}

class ListVisualizerTest extends Scene {

  {
    val pane = new Pane()
    pane.setPrefSize(1200, 900)
    getChildren.add(pane)

    val button = Button("Next step")
    getChildren.add(button)



    val list = fill(List(1, 2, 3, 4))
    var i: Int = 0

    val positions: List[(Int, Int)] = List(
      (600, 50),
      (400, 200), (800, 200),
      (300, 350), (500, 350), (700, 350), (900, 350),
    )
    button.setOnAction(_ => {
      ListVisualizer.visualize(pane, positions(i), list(i))
      i += 1
    })
  }

  def fill(list: List[Int]): List[List[Int]] =
    var result: List[List[Int]] = Nil
    var i = list.size
    var j = 1
    while i != 0 do {
      for (sublist <- list.grouped(list.size / j)) {
        result = result :+ sublist
      }
      j *= 2
      i >>>= 1
    }
    result
}
