package com.peknight.generic.deriving

import com.peknight.generic.deriving.JsonValue.*
import com.peknight.generic.deriving.Shape.*
import com.peknight.generic.scalacheck.instances.arbitrary.given
import org.scalacheck.Prop.forAll
import org.scalacheck.Properties

class JsonEncoderSpecification extends Properties("JsonEncoder"):

  type F[A] = A match {case (label, value) => (label, value match {
    case String => JsonString
    case Int => JsonNumber
    case Boolean => JsonBoolean
    case Double => JsonNumber
  })}

  def toJsonObject(labels: Tuple, repr: Tuple): JsonObject = JsonObject(
    labels.zip(repr).map[F] { [T] => (t: T) =>
      val (label, value) = t: @unchecked
      value match
        case s: String => (label, JsonString(s)).asInstanceOf[F[T]]
        case i: Int => (label, JsonNumber(i)).asInstanceOf[F[T]]
        case b: Boolean => (label, JsonBoolean(b)).asInstanceOf[F[T]]
        case d: Double => (label, JsonNumber(d)).asInstanceOf[F[T]]
    }.toList.asInstanceOf[List[(String, JsonValue)]]
  )

  property("should have an instance for IceCream") = forAll { (iceCream: IceCream) =>
    JsonEncoder[IceCream].encode(iceCream) == toJsonObject(Mirror.labels[IceCream], Tuple.fromProductTyped(iceCream))
  }

  property("should have an instance for Shape") = forAll { (shape: Shape) =>
    val inst = Generic.Sum.Instances[Generic.Product, Shape].instance(shape)
    val jsonObject = toJsonObject(inst.labels, inst.to(shape))
    JsonEncoder[Shape].encode(shape) == JsonObject(List((Generic.Sum[Shape].label(shape), jsonObject)))
  }
end JsonEncoderSpecification
