package ch.epfl.alcmp.gui

import javafx.scene.control.{Button, Label, TextArea, TextField}
import javafx.scene.input.KeyCode
import javafx.scene.layout.GridPane
import scalafx.application.Platform
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.Scene
import scalafx.scene.layout.{HBox, Pane, VBox}

object DCSpecs1Scene extends Scene {

  private val vbox = VBox()

  {

    val title = new Label("Specifications (1/2)")
    title.getStyleClass.add("title-text")

    val divide = new Pane()
    val divideText = new Label("Divide procedure")
    divideText.setPrefSize(270, 70)
    divide.getChildren.add(divideText)
    divideText.getStyleClass.add("field-text")

    val divideField = new TextArea()
    divideField.getStyleClass.add("field-box")
    divideField.setPrefWidth(600)
    divideField.setPrefHeight(300)
    divideField.setPromptText("return arg[0:n//2], arg[n//2:n]")

    val baseCase = new Pane()
    val baseCaseText = new Label("Base case")
    baseCaseText.setPrefSize(270, 70)

    baseCase.getChildren.add(baseCaseText)
    baseCaseText.getStyleClass.add("field-text")

    val baseCaseField = new TextArea()
    baseCaseField.getStyleClass.add("field-box")
    baseCaseField.setPrefWidth(600)
    baseCaseField.setPrefHeight(100)
    baseCaseField.setPromptText("if n == 1: return arg")

    val grid = GridPane()

    grid.getStyleClass.add("input-grid")
    grid.add(divide, 0, 1)
    grid.add(divideField, 1, 1)
    grid.add(baseCase, 0, 2)
    grid.add(baseCaseField, 1, 2)
    grid.setPadding(Insets(50, 50, 50, 50))
    grid.setHgap(50)
    grid.setVgap(20)

    val previous = Button("Previous")
    previous.getStyleClass.add("button")
    previous.setPrefWidth(200)
    val previousBox = VBox()
    previousBox.getChildren.add(previous)

    val next = Button("Next")
    next.getStyleClass.add("button")
    next.setPrefWidth(200)
    val nextBox = VBox()
    nextBox.getChildren.add(next)

    val hole = new Label() // i mean...
    hole.setPrefWidth(640)

    val buttonsRow = HBox()
    buttonsRow.setPrefWidth(ScalaFXMain.WIDTH)
    buttonsRow.getChildren.addAll(previousBox, hole, nextBox)

    vbox.setPrefSize(ScalaFXMain.WIDTH, ScalaFXMain.HEIGHT)
    vbox.getStyleClass.add("main-specs1-box")
    vbox.getStylesheets.add("css/dc-specs1.css")
    vbox.getChildren.addAll(title, grid, buttonsRow)

    getChildren.add(vbox)

    Platform.runLater(() => vbox.requestFocus()) // takes focus off textfield

    this.setOnKeyPressed(e => e.getCode match {
      case KeyCode.ESCAPE => Platform.runLater(() => vbox.requestFocus())
      case KeyCode.ENTER => next.fire()
      case KeyCode.RIGHT => next.fire()
      case KeyCode.LEFT => previous.fire()
      case _ => ()
    })

    next.setOnAction(_ => {
      ScalaFXMain.stage.scene = DCSpecs2Scene
    })

    previous.setOnAction(_ => {
      ScalaFXMain.stage.scene = DCOverviewScene
    })
  }

}
