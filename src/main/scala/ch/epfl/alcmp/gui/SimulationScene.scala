package ch.epfl.alcmp.gui

import javafx.scene.Node
import javafx.scene.control.{Button, Label}
import javafx.scene.layout.{BorderPane, HBox, VBox, Pane}
import scalafx.scene.Scene

object SimulationScene extends Scene {

  private val mainPane = BorderPane()

  {
    mainPane.getStylesheets.add("css/simulator.css")
    mainPane.getStyleClass.add("main-pane")

    val title = Label("Simulator")
    title.getStyleClass.add("title-text")
    title.setPrefWidth(ScalaFXMain.WIDTH)
    mainPane.setTop(title)

    val simulationPane = createSimulationPane()
    //val menuPane = ???
    val center = HBox(simulationPane)
    center.setPrefSize(400, 400)
    mainPane.setCenter(center)

    mainPane.setPrefSize(ScalaFXMain.WIDTH, ScalaFXMain.HEIGHT) //Huh...?
    getChildren.add(mainPane)
  }

  private def createSimulationPane(): Node =
    val simulationPane = BorderPane()

    val beginning = createButton("Beginning")
    val previous = createButton("Previous")
    val play = createButton("⏯")
    val next = createButton("Next")
    val end = createButton("End")
    val buttons = HBox(beginning, previous, play, next, end)
    buttons.setPrefSize(ScalaFXMain.WIDTH/2, 200)
    simulationPane.setBottom(buttons)

    val simulation = Pane()
    simulationPane.setCenter(simulation)
    simulationPane.setPrefSize(ScalaFXMain.WIDTH/2, 200)
    simulationPane.getStyleClass.add("simulation-pane")
    simulationPane

  private def createButton(name : String): Node =
    val button = Button(name)
    button.getStyleClass.add("simulation-button")
    button.setPrefWidth(ScalaFXMain.WIDTH/10)
    button
}
