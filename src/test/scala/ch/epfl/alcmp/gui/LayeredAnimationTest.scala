package ch.epfl.alcmp.gui

import ch.epfl.alcmp.data.{IList, SimulationData}
import ch.epfl.alcmp.gui.{PathVisualizer, Position}
import ch.epfl.alcmp.net.SimulationMessage.DivideMessage
import ch.epfl.alcmp.anim.LayeredAnimationCreator
import javafx.animation.SequentialTransition
import javafx.scene.control.Button
import scalafx.scene.Scene
import scalafx.scene.layout.Pane

class LayeredAnimationTest extends Scene {

  {
    val pane = new Pane()
    pane.setPrefSize(1200, 900)
    getChildren.add(pane)

    val button = Button("Next step")
    getChildren.add(button)

    val data = SimulationData()

    val l00 = IList(List(1, 2, 3, 4, 5, 6, 7, 8))
    val l10 = IList(List(1, 2, 3, 4))
    val l11 = IList(List(5, 6, 7, 8))
    val l20 = IList(List(1, 2))
    val l21 = IList(List(3, 4))
    val l22 = IList(List(5, 6))
    val l23 = IList(List(7, 8))
    val l30 = IList(List(1))
    val l31 = IList(List(2))
    val l32 = IList(List(3))
    val l33 = IList(List(4))
    val l34 = IList(List(5))
    val l35 = IList(List(6))
    val l36 = IList(List(7))
    val l37 = IList(List(8))


    val dm0 = DivideMessage(0, 0, 0, List(l00), Nil)
    val dm1 = DivideMessage(0, 1, 0, List(l10, l11), Nil)
    val dm2 = DivideMessage(0, 2, 0, List(l20, l21), Nil)
    val dm3 = DivideMessage(0, 2, 2, List(l22, l23), Nil)
    val dm4 = DivideMessage(0, 3, 0, List(l30, l31), Nil)
    val dm5 = DivideMessage(0, 3, 2, List(l32, l33), Nil)
    val dm6 = DivideMessage(0, 3, 4, List(l34, l35), Nil)
    val dm7 = DivideMessage(0, 3, 6, List(l36, l37), Nil)

    data.addDivisionData(dm0)
    data.addDivisionData(dm1)
    data.addDivisionData(dm2)
    data.addDivisionData(dm3)
    data.addDivisionData(dm4)
    data.addDivisionData(dm5)
    data.addDivisionData(dm6)
    data.addDivisionData(dm7)


    button.setOnAction(_ => {
      val anims = LayeredAnimationCreator(data)
      val topAnim = SequentialTransition()
      for (anim <- anims.computeSteps(pane)) {
        topAnim.getChildren.add(anim)
      }
      topAnim.play()
    })
  }
}
