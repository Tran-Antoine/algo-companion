package ch.epfl.alcmp.net

import ch.epfl.alcmp.data.InputType

case class DivideMessage[T <: InputType](id: Int, depth: Int, index: Int, outputs: List[T], highlights: List[Int])
