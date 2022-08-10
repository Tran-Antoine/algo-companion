package ch.epfl.alcmp.gui

import ch.epfl.alcmp.data.{IBinaryTree, IHeap, IList, IMatrix, InputType}
import javafx.animation.Animation
import javafx.scene.layout.Pane
import javafx.scene.Node

trait Visualizer[T <: Visualizable] {
  def visualize(parent: Pane, obj: T, pos: Position): Animation
}

object Visualizer {

  given Visualizer[VisualizableList] = ListVisualizer
  given Visualizer[VisualizableLine] = PathVisualizer

  def visualize[T <: Visualizable](using v: Visualizer[T])(parent: Pane, obj: T, pos: Position): Animation =
    v.visualize(parent, obj, pos)

  def convert(in: InputType)(pane: Pane, pos: Position): Animation = in match {
    case IList(list) => ListVisualizer.visualize(pane, VisualizableList(list), pos)
    case IMatrix(rows) => ???
    case IHeap(list) => ???
    case IBinaryTree(root) => ???
  }
}

opaque type Position = (Int, Int)
object Position {

  val NULL: Position = Position(-1, -1)

  extension (pos: Position) {
    def x: Int = pos._1
    def y: Int = pos._2

    def addX(v: Int): Position = (x+v, y)
    def addY(v: Int): Position = (x, y+v)
  }

  def apply(pos: (Int, Int)): Position = pos
}
