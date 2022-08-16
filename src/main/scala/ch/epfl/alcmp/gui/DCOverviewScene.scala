package ch.epfl.alcmp.gui

import ch.epfl.alcmp.parsing.RecurrenceCalculator
import javafx.scene.control.{Button, Label, TextField}
import javafx.scene.layout.{Background, Border, ColumnConstraints, GridPane, RowConstraints}
import scalafx.application.Platform
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.effect.DropShadow
import scalafx.scene.layout.{HBox, Pane, Priority, VBox}
import scalafx.scene.paint.{Color, LinearGradient, Stops}
import scalafx.scene.paint.Color.{DarkGray, DarkRed, Red, White, color}
import scalafx.scene.text.Text
import scalafx.geometry.Pos
import javafx.geometry.HPos
import javafx.geometry.VPos
import javafx.scene.input.KeyCode
import scalafx.scene.control.ComboBox

object DCOverviewScene extends Scene {

  private val vbox = VBox()

  {
    val title = new Label("Algorithm Overview")
    title.getStyleClass.add("title-text")


    val recurrence = new Pane()
    val recurrenceText = new Label("Runtime recurrence")
    recurrenceText.getStyleClass.add("field-text")
    recurrenceText.setPrefSize(270, 70)
    recurrence.getChildren.add(recurrenceText)

    val recurrenceResult = new Pane()
    val recurrenceResultText = new Label()
    recurrenceResultText.getStyleClass.add("field-text-result")
    recurrenceResultText.setPrefSize(200, 70)
    recurrenceResult.getChildren.add(recurrenceResultText)

    val recurrenceField = new TextField()
    recurrenceField.getStyleClass.add("field-box")
    recurrenceField.setPrefWidth(400)
    recurrenceField.setPromptText("T(n) = 2T(n/2) + O(n)")

    recurrenceField.textProperty().addListener((_, _, newValue) => {

      recurrenceField.getStyleClass.removeAll("green-border", "orange-border", "red-border")

      if (newValue.nonEmpty) {
        val result = RecurrenceCalculator.complexity(newValue.replaceAll("\\s", ""))

        result match {
          case None => recurrenceField.getStyleClass.add("red-border")
          case Some(complexity, exact) =>
            recurrenceField.getStyleClass.add(if exact then "green-border" else "orange-border")
            val prefix = if exact then "Θ" else "O"
            recurrenceResultText.setText(s"$prefix($complexity)")
        }
      }
    })

    val inputType = new Pane()
    val inputTypeText = new Label("Input type")
    inputTypeText.getStyleClass.add("field-text")
    inputTypeText.setPrefSize(270, 70)
    inputType.getChildren.add(inputTypeText)

    val inputTypeField = new ComboBox(List("List", "Matrix", "Heap", "BinaryTree"))
    inputTypeField.getStyleClass.add("field-box")

    val grid = GridPane()

    grid.getStyleClass.add("input-grid")
    grid.add(recurrence, 0, 1)
    grid.add(recurrenceField, 1, 1)
    grid.add(recurrenceResult, 2, 1)
    grid.add(inputType, 0, 3)
    grid.add(inputTypeField, 1, 3)
    grid.setPadding(Insets(50, 10, 50, 10))
    grid.setAlignment(Pos.Center)
    grid.setHgap(60)
    grid.setVgap(40)

    val next = Button("Next")
    next.getStyleClass.add("next-button")
    next.setPrefWidth(200)
    val buttonBox = VBox()
    buttonBox.getChildren.add(next)
    buttonBox.setAlignment(Pos.BottomRight)

    vbox.getStylesheets.add("css/dc-overview.css")
    vbox.getStyleClass.add("main-overview-box")
    vbox.setPrefSize(ScalaFXMain.WIDTH, ScalaFXMain.HEIGHT) // JavaFX is the worst thing I've ever seen
    vbox.getChildren.addAll(title, grid, buttonBox)

    getChildren.add(vbox)
    Platform.runLater(() => vbox.requestFocus()) // takes focus off textfield

    this.setOnKeyPressed(e => e.getCode match {
      case KeyCode.ESCAPE => Platform.runLater(() => vbox.requestFocus())
      case KeyCode.ENTER => next.fire()
      case KeyCode.RIGHT => next.fire()
      case _ => ()
    })

    next.setOnAction(_ => {
      ScalaFXMain.stage.scene = DCSpecs1Scene
    })
  }
}
