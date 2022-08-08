package ch.epfl.alcmp.gui

import ch.epfl.alcmp.data.BinaryTree

import scala.language.implicitConversions

trait Visualizable

case class VisualizableLine(start: Position, end: Position) extends Visualizable
case class VisualizableList(list: List[Int]) extends Visualizable
case class VisualizableMatrix(rows: List[List[Int]]) extends Visualizable
case class VisualizableHeap(list: List[Int]) extends Visualizable
case class VisualizableBinaryTree(root: BinaryTree[Int]) extends Visualizable

implicit def lineToObj(line: (Position, Position)): VisualizableLine = VisualizableLine(line._1, line._2)
implicit def listToObj(list: List[Int]): VisualizableList = VisualizableList(list)
implicit def matrixToObj(rows: List[List[Int]]): VisualizableMatrix = VisualizableMatrix(rows)
implicit def heapToObj(list: List[Int]): VisualizableHeap = VisualizableHeap(list)
implicit def binaryTreeToObj(root: BinaryTree[Int]): VisualizableBinaryTree = VisualizableBinaryTree(root)