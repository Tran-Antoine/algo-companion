package ch.epfl.alcmp.gui

import ch.epfl.alcmp.gui.DCSpecs1Scene.getChildren
import javafx.scene.control.{Button, Label, TextArea}
import javafx.scene.input.KeyCode
import javafx.scene.layout.GridPane
import scalafx.application.Platform
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.layout.{HBox, Pane, VBox}

object DCSpecs2Scene extends Scene {
  private val vbox = VBox()

  {

    val title = new Label("Specifications (2/2)")
    title.getStyleClass.add("title-text")

    val combine = new Pane()
    val combineText = new Label("Combine procedure")
    combineText.setPrefSize(270, 70)
    combine.getChildren.add(combineText)
    combineText.getStyleClass.add("field-text")

    val combineField = new TextArea()
    combineField.getStyleClass.add("field-box")
    combineField.setPrefWidth(600)
    combineField.setPrefHeight(420)

    val combineTextExample =
      """i = 0; j = 0; res = []
        |for _ in range(n):
        |    if arg0[i] < arg1[j]:
        |        res.append(arg0[i])
        |        i += 1
        |    else:
        |        res.append(arg1[j])
        |        j += 1
        |return res
        |""".stripMargin
    combineField.setPromptText(combineTextExample)

    val grid = GridPane()

    grid.getStyleClass.add("input-grid")
    grid.add(combine, 0, 1)
    grid.add(combineField, 1, 1)
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
    hole.setPrefWidth(800)

    val buttonsRow = HBox()
    buttonsRow.setPrefWidth(ScalaFXMain.WIDTH)
    buttonsRow.getChildren.addAll(previousBox, hole, nextBox)

    vbox.setPrefSize(ScalaFXMain.WIDTH, ScalaFXMain.HEIGHT)
    vbox.getStyleClass.add("main-specs2-box")
    vbox.getStylesheets.add("css/dc-specs2.css")
    vbox.getChildren.addAll(title, grid, buttonsRow)

    getChildren.add(vbox)

    Platform.runLater(() => vbox.requestFocus()) // takes focus off texfield

    this.setOnKeyPressed(e => e.getCode match {
      case KeyCode.ESCAPE => Platform.runLater(() => vbox.requestFocus())
      case KeyCode.ENTER => next.fire()
      case KeyCode.RIGHT => next.fire()
      case KeyCode.LEFT => previous.fire()
      case _ => ()
    })

    next.setOnAction(_ => {
      ScalaFXMain.stage.scene = SimulationScene
    })

    previous.setOnAction(_ => {
      ScalaFXMain.stage.scene = DCSpecs1Scene
    })
  }
}
