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

    val l00 = IList(List(1, 2, 3, 4))
    val l10 = IList(List(1, 2))
    val l11 = IList(List(3, 4))
    val l20 = IList(List(1))
    val l21 = IList(List(2))
    val l22 = IList(List(3))
    val l23 = IList(List(4))

    val dm0 = DivideMessage(0, 0, 0, List(l00), Nil)
    val dm1 = DivideMessage(0, 1, 0, List(l10, l11), Nil)
    val dm2 = DivideMessage(0, 2, 0, List(l20, l21), Nil)
    val dm3 = DivideMessage(0, 2, 1, List(l22, l23), Nil)

    data.addDivisionData(dm0)
    data.addDivisionData(dm1)
    data.addDivisionData(dm2)
    data.addDivisionData(dm3)


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
