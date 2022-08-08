package ch.epfl.alcmp.gui

import javafx.animation.Animation
import javafx.scene.layout.Pane
import javafx.scene.Node

trait Visualizer[T] {
  def visualize(parent: Pane, obj: T, pos: Position): Animation
}

opaque type Position = (Int, Int)
object Position {

  val NULL: Position = Position(-1, -1)

  extension (pos: Position) {
    def x: Int = pos._1
    def y: Int = pos._2
  }
  def apply(pos: (Int, Int)): Position = pos
}
