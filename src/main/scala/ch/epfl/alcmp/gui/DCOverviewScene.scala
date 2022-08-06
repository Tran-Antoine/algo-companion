package ch.epfl.alcmp.gui

import javafx.scene.control.{Label, TextField}
import javafx.scene.layout.{Background, ColumnConstraints, GridPane, RowConstraints}
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



    vbox.getStylesheets.add("css/dc-overview.css")
    vbox.getStyleClass.add("main-overview-box")
    vbox.setPrefSize(1200, 900) // JavaFX is the worst thing I've ever seen
    vbox.getChildren.addAll(title, grid)

    getChildren.add(vbox)
    Platform.runLater(() => vbox.requestFocus()) // takes focus off texfield
  }
}
