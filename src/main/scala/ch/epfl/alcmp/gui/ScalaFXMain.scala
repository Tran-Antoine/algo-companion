package ch.epfl.alcmp.gui

import javafx.scene.image.Image
import scalafx.application.JFXApp3

object ScalaFXMain extends JFXApp3 {

  val WIDTH = 1200
  val HEIGHT = 900

  override def start(): Unit = {
    stage = new JFXApp3.PrimaryStage {
      title = "Algo Companion"
      width = ScalaFXMain.WIDTH
      height = ScalaFXMain.HEIGHT
      scene = DCOverviewScene
      icons.add(new Image(ScalaFXMain.getClass.getResourceAsStream("/logo.png")))
    }
  }
}
