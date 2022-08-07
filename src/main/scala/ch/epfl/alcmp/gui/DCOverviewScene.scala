package ch.epfl.alcmp.gui

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

object DCOverviewScene extends Scene {

  private val vbox = VBox()

  {
    val title = new Label("Algorithm Overview")
    title.getStyleClass.add("title-text")

    val recurrenceText = new Label("Runtime recurrence")
    recurrenceText.getStyleClass.add("field-text")

    val recurrenceField = new TextField()
    recurrenceField.getStyleClass.add("field-box")
    recurrenceField.setPrefWidth(350)
    recurrenceField.setPromptText("T(n) = 2T(n/2) + O(n)")

    val recurrenceResult = new Label("O(nlog(n))")
    recurrenceResult.getStyleClass.add("field-text")

    val inputTypeText = new Label("Input type")
    inputTypeText.getStyleClass.add("field-text")

    val inputTypeField = new TextField()
    inputTypeField.getStyleClass.add("field-box")
    inputTypeField.setPromptText("List, Matrix, Heap ...")
    inputTypeField.textProperty().addListener((_, _, newValue) => {
      val values = Set("List", "Matrix", "Heap", "BinaryTree")
      inputTypeField.getStyleClass.removeAll("green-border", "red-border")

      if(values.contains(newValue)) {
        inputTypeField.getStyleClass.add("green-border")
        inputTypeField.getStyleClass.remove("red-border")
      } else if(newValue.nonEmpty) {
        inputTypeField.getStyleClass.add("red-border")
        inputTypeField.getStyleClass.remove("green-border")
      }
    })

    val grid = GridPane()

    grid.getStyleClass.add("input-grid")
    grid.add(recurrenceText, 0, 1)
    grid.add(recurrenceField, 1, 1)
    grid.add(recurrenceResult, 2, 1)
    grid.add(inputTypeText, 0, 3)
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
    vbox.setPrefSize(1200, 900) // JavaFX is the worst thing I've ever seen
    vbox.getChildren.addAll(title, grid, buttonBox)

    getChildren.add(vbox)
    Platform.runLater(() => vbox.requestFocus()) // takes focus off texfield

    vbox.setOnKeyPressed(e => e.getCode match {
      case KeyCode.ESCAPE => Platform.runLater(() => vbox.requestFocus())
      case KeyCode.ENTER => next.fire()
      case _ => ()
    })

    next.setOnAction(_ => {
      ScalaFXMain.stage.scene = DCSpecs1Scene
    })
  }
}
