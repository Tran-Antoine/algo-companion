package ch.epfl.alcmp.data

sealed trait InputType {
  def getType: TypeId
}

case class IList(list: List[Int]) extends InputType {
  override def getType: TypeId = TypeId.ListType
}

case class IMatrix(rows: List[List[Int]]) extends InputType {
  override def getType: TypeId = TypeId.MatrixType
}

case class IHeap(list: List[Int]) extends InputType {
  override def getType: TypeId = TypeId.HeapType
}

case class IBinaryTree(root: BinaryTree[Int]) extends InputType {
  override def getType: TypeId = TypeId.BinaryTreeType
}

enum TypeId {
  case ListType, MatrixType, HeapType, BinaryTreeType
}

