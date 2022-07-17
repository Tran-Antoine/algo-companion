package ch.epfl.alcmp.data

sealed trait InputType

object InputType {
  trait BinaryTree[T]
  case class Leaf[T]() extends BinaryTree[T]
  case class Node[T](value: T, left: BinaryTree[T], right: BinaryTree[T]) extends BinaryTree[T]

  type IList = List[Int] with InputType
  type IMatrix = List[List[Int]] with InputType
  type IHeap = List[Int] with InputType
  type IBinaryTree = BinaryTree[Int] with InputType
}

