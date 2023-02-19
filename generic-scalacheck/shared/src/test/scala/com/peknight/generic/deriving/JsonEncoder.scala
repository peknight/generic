package com.peknight.generic.deriving

import com.peknight.generic.deriving.JsonValue.*
import com.peknight.generic.tuple.syntax.foldRight

trait JsonEncoder[A]:
  def encode(value: A): JsonValue
end JsonEncoder

object JsonEncoder:
  def apply[A](using enc: JsonEncoder[A]): JsonEncoder[A] = enc
  def createEncoder[A](func: A => JsonValue): JsonEncoder[A] = func(_)

  given JsonEncoder[String] with
    def encode(value: String): JsonValue = JsonString(value)
  end given

  given JsonEncoder[Double] with
    def encode(value: Double): JsonValue = JsonNumber(value)
  end given

  given JsonEncoder[Int] with
    def encode(value: Int): JsonValue = JsonNumber(value)
  end given

  given JsonEncoder[Boolean] with
    def encode(value: Boolean): JsonValue = JsonBoolean(value)
  end given

  given[A] (using enc: JsonEncoder[A]): JsonEncoder[List[A]] with
    def encode(value: List[A]): JsonValue = JsonArray(value.map(enc.encode))
  end given

  given[A] (using enc: JsonEncoder[A]): JsonEncoder[Option[A]] with
    def encode(value: Option[A]): JsonValue = value.map(enc.encode).getOrElse(JsonNull)
  end given

  given JsonObjectEncoder[EmptyTuple] with
    def encode(value: EmptyTuple): JsonObject = JsonObject(Nil)
  end given

  inline given [A](using instances: => Generic.Instances[JsonEncoder, A]): JsonEncoder[A] = instances.derive(
    inst ?=> JsonObjectEncoder.createObjectEncoder(
      (value: A) => JsonObject(inst.mapWithLabel[[_] =>> (String, JsonValue)](value)(
        [T] => (enc: JsonEncoder[T], t: T, label: String) => (label, enc.encode(t))
      ).foldRight(List.empty[(String, JsonValue)])(
        [T] => (t: T, acc: List[(String, JsonValue)]) => t.asInstanceOf[(String, JsonValue)] :: acc
      )
    )),
    inst ?=> JsonObjectEncoder.createObjectEncoder((value: A) => JsonObject(List(inst.withLabel(value)(
      (encoder, label) => label -> encoder.encode(value)
    ))))
  )

end JsonEncoder
