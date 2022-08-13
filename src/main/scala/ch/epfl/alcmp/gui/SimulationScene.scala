package ch.epfl.alcmp.gui

import javafx.scene.Node
import javafx.scene.control.ButtonBar.ButtonData
import javafx.scene.control.{Button, ButtonBar, ButtonType, DialogPane, Label, PopupControl, TextArea, TextInputDialog}
import javafx.scene.layout.{Background, BorderPane, HBox, Pane, VBox}
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.Scene

object SimulationScene extends Scene {

  private val mainPane = HBox()

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

    val menuPane = createMenuPane()
    val simulationPane = createSimulationPane()

    previousScreen.getStyleClass.add("prev-button")

    val intermediatePane = VBox(title, menuPane)
    intermediatePane.getStyleClass.add("center-pane")

    val leftBox = VBox(intermediatePane, previousScreen)
    leftBox.setPrefWidth(MENU_WIDTH)
    leftBox.setSpacing(10)

    mainPane.getChildren.add(leftBox)
    mainPane.getChildren.add(simulationPane)

    mainPane.setPrefSize(ScalaFXMain.WIDTH, ScalaFXMain.HEIGHT) //Huh...?
    getChildren.add(mainPane)

    previousScreen.setOnAction(_ => {
      ScalaFXMain.stage.scene = DCSpecs2Scene
    })
    setupMenuHandlers()
    setupSimulationHandlers()
  }

  /**
   * Creates pane containing the menu buttons
   */
  private def createMenuPane(): Node =
    inputDialog.setHeaderText(null)
    inputDialog.setTitle("Input")
    inputDialog.setContentText("Enter Input : ")
    inputDialog.setGraphic(null)
    val pane = inputDialog.getDialogPane
    pane.getStylesheets.add("css/simulator.css")
    pane.getButtonTypes.setAll(ButtonType.OK, ButtonType("Random", ButtonBar.ButtonData.CANCEL_CLOSE))
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
      val javaOptional = inputDialog.showAndWait()
      val userResponse = if javaOptional.isPresent then Some(javaOptional.get()) else None
      userResponse match
        case Some(response) =>
          println("Your input was" + response)
          //parse input
        case None =>
          println("Random input is being generated")
          //generate random input
    })

  /**
   * Creates pane containing the simulation and the simulation buttons
   */
  private def createSimulationPane(): Node =
    val simulationPane = BorderPane()

    val buttons = HBox()
    List(beginning, previous, play, next, end).foreach(button => buttons.getChildren.add(setupSimulationButton(button)))
    simulationPane.setBottom(buttons)
    buttons.getStyleClass.add("simulation-buttons")

    val simulation = Pane() //Just put the node that does the simulation in here I guess
    simulation.getStyleClass.add("simulation-pane")
    simulationPane.setCenter(simulation)

    simulationPane.setPrefSize(SIMULATION_WIDTH, 200)
    simulationPane

  private def setupSimulationButton(button: Button): Button =
    button.getStyleClass.add("simulation-button")
    button.setPrefWidth(SIMULATION_WIDTH / NB_SIMULATION_BUTTONS - 15)
    button

  private def setupSimulationHandlers(): Unit = {}

}