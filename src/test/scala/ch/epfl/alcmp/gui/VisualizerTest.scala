package ch.epfl.alcmp.gui

import ch.epfl.alcmp.gui.ScalaFXMain.stage
import javafx.scene.image.Image
import scalafx.application.JFXApp3

object VisualizerTest extends JFXApp3 {

  val WIDTH = 1200
  val HEIGHT = 900

  override def start(): Unit = {
    stage = new JFXApp3.PrimaryStage {
      title = "Algo Companion"
      width = ScalaFXMain.WIDTH
      height = ScalaFXMain.HEIGHT
      scene = LayeredAnimationTest()
      icons.add(new Image(ScalaFXMain.getClass.getResourceAsStream("/logo.png")))
    }
  }
}
