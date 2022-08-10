package ch.epfl.alcmp.gui

import javafx.scene.Node
import javafx.scene.control.{Button, ButtonType, DialogPane, Label, PopupControl, TextArea, TextInputDialog}
import javafx.scene.layout.{BorderPane, HBox, Pane, VBox}
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.Scene

object SimulationScene extends Scene {

  private val mainPane = VBox()

  private val previousScreen = Button("Previous")

  //Simulation buttons
  private val beginning = Button("Beginning")
  private val previous = Button("Previous")
  private val play = Button("⏯")
  private val next = Button("Next")
  private val end = Button("End")

  //Menu buttons
  private val inputButton = Button("Input")
  private val inputDialog = TextInputDialog()
  private val startButton = Button("Start Simulation")
  private val verifierButton = Button("Use Verifier")
  private val exportButton = Button("Export Algorithm")

  private val FULL_WIDTH = ScalaFXMain.WIDTH - 160
  private val PROPORTIONS = (1.0/4, 3.0/4) //should add up to 1

  private val MENU_WIDTH = FULL_WIDTH * PROPORTIONS._1

  private val SIMULATION_WIDTH = FULL_WIDTH * PROPORTIONS._2
  private val NB_SIMULATION_BUTTONS = 5

  {
    mainPane.getStylesheets.add("css/simulator.css")
    mainPane.getStyleClass.add("main-pane")

    val title = Label("Simulator")
    title.getStyleClass.add("title-text")
    title.setPrefWidth(ScalaFXMain.WIDTH)
    mainPane.getChildren.add(title)

    val simulationPane = createSimulationPane()
    val menuPane = createMenuPane()
    val center = HBox(menuPane, simulationPane)
    center.getStyleClass.add("center-pane")
    mainPane.getChildren.add(center)

    previousScreen.getStyleClass.add("prev-button")
    mainPane.getChildren.add(previousScreen)

    mainPane.setPrefSize(ScalaFXMain.WIDTH, ScalaFXMain.HEIGHT) //Huh...?
    getChildren.add(mainPane)

    previousScreen.setOnAction(_ => {
      ScalaFXMain.stage.scene = DCSpecs2Scene
    })
    setupMenuHandlers()
    setupSimulationHandlers()
  }

  private def createMenuPane(): Node =
    inputDialog.setHeaderText(null)
    inputDialog.setTitle("Input")
    inputDialog.setContentText("Enter Input : ")
    inputDialog.setGraphic(null)
    val pane = inputDialog.getDialogPane
    pane.getStylesheets.add("css/simulator.css")
    pane.getButtonTypes.setAll(ButtonType("Ok"), ButtonType("Random"))
    //Might be really useful! If only I could understand how all this works ...

    val menuPane = VBox()
    List(inputButton, startButton, verifierButton, exportButton).foreach(b => menuPane.getChildren.add(setupMenuButton(b)))
    menuPane.getStyleClass.add("menu-pane")
    menuPane.setPrefWidth(MENU_WIDTH)
    menuPane

  private def setupMenuButton(button : Button): Button =
    button.getStyleClass.add("menu-button")
    button

  private def setupMenuHandlers(): Unit =
    inputButton.setOnAction(_ => {
      inputDialog.show()
    })

  private def createSimulationPane(): Node =
    val simulationPane = BorderPane()

    val buttons = HBox()
    List(beginning, previous, play, next, end).foreach(button => buttons.getChildren.add(setupSimulationButton(button)))
    simulationPane.setBottom(buttons)

    val simulation = Pane() //I guess something else should go here
    simulation.getStyleClass.add("simulation-pane")
    simulationPane.setCenter(simulation)

    simulationPane.setPrefSize(SIMULATION_WIDTH, 200)
    simulationPane

  private def setupSimulationButton(button: Button): Button =
    button.getStyleClass.add("simulation-button")
    button.setPrefWidth(SIMULATION_WIDTH / NB_SIMULATION_BUTTONS)
    button

  private def setupSimulationHandlers(): Unit = {}

}