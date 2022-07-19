package ch.epfl.alcmp.net

import ch.epfl.alcmp.data.TypeId

object Serdes {
  def serialize[T](using serde: Serde[T])(obj: T): String = serde.serialize(obj)
  def deserialize[T](using serde: Serde[T])(data: String): T = serde.deserialize(data)
  def serialize[T](using serdeFun: TypeId => Serde[T])(typeId: TypeId, obj: T): String =
    serdeFun.apply(typeId).serialize(obj)
  def deserialize[T](using serdeFun: TypeId => Serde[T])(typeId: TypeId, data: String): T =
    serdeFun.apply(typeId).deserialize(data)
}
