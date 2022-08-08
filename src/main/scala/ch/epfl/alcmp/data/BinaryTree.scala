package ch.epfl.alcmp.data

sealed trait BinaryTree[T]

case class Leaf[T]() extends BinaryTree[T]
case class Node[T](value: T, left: BinaryTree[T], right: BinaryTree[T]) extends BinaryTree[T]