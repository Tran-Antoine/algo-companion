package ch.epfl.alcmp.gui

import scalafx.application.JFXApp3

object ScalaFXMain extends JFXApp3 {
  override def start(): Unit = {
    stage = new JFXApp3.PrimaryStage {
      title = "Algo Companion"
      width = 1200
      height = 900
      scene = DCOverviewScene
    }
  }
}
