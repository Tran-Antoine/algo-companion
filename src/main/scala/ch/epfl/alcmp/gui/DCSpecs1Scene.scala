package ch.epfl.alcmp.gui

import ch.epfl.alcmp.gui.DCOverviewScene.vbox
import javafx.scene.control.{Label, TextArea, TextField}
import javafx.scene.input.KeyCode
import javafx.scene.layout.{GridPane, VBox}
import scalafx.application.Platform
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.Scene

object DCSpecs1Scene extends Scene {

  private val vbox = VBox()

  {
    val title = new Label("Specifications (1/2)")
    title.getStyleClass.add("title-text")

    val divideText = new Label("Divide procedure")
    divideText.getStyleClass.add("field-text")

    val divideField = new TextArea()
    divideField.getStyleClass.add("field-box")
    divideField.setPrefWidth(600)
    divideField.setPrefHeight(500)
    divideField.setPromptText("return input0[0:n//2], input0[n//2:n]")

    val baseCaseText = new Label("Base case")
    baseCaseText.getStyleClass.add("field-text")

    val baseCaseField = new TextArea()
    baseCaseField.getStyleClass.add("field-box")
    baseCaseField.setPrefWidth(600)
    baseCaseField.setPrefHeight(200)
    baseCaseField.setPromptText("if n == 1: return input0")

    val grid = GridPane()

    grid.getStyleClass.add("input-grid")
    grid.add(divideText, 0, 1)
    grid.add(divideField, 1, 1)
    grid.add(baseCaseText, 0, 3)
    grid.add(baseCaseField, 1, 3)
    grid.setPadding(Insets(50, 50, 50, 50))
    grid.setAlignment(Pos.Center)
    grid.setHgap(60)
    grid.setVgap(40)

    vbox.setPrefSize(1200, 900)
    vbox.getStyleClass.add("main-specs1-box")
    vbox.getStylesheets.add("css/dc-specs1.css")
    vbox.getChildren.addAll(title, grid)

    getChildren.add(vbox)

    Platform.runLater(() => vbox.requestFocus()) // takes focus off texfield

    vbox.setOnKeyPressed(e => e.getCode match {
      case KeyCode.ESCAPE => Platform.runLater(() => vbox.requestFocus())
      //case KeyCode.ENTER => next.fire()
      case _ => ()
    })
  }

}
