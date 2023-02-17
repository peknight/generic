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

  def toJsonObject[A <: Product : Mirror.Product](labels: Tuple, a: A): JsonObject = JsonObject(
    labels.zip(Tuple.fromProductTyped(a)).map[F] { [T] => (t: T) =>
      val (label, value) = t: @unchecked
      value match
        case s: String => (label, JsonString(s)).asInstanceOf[F[T]]
        case i: Int => (label, JsonNumber(i)).asInstanceOf[F[T]]
        case b: Boolean => (label, JsonBoolean(b)).asInstanceOf[F[T]]
        case d: Double => (label, JsonNumber(d)).asInstanceOf[F[T]]
    }.toList.asInstanceOf[List[(String, JsonValue)]]
  )

  val iceCreamLabels = Mirror.labels[IceCream]

  property("should have an instance for IceCream") = forAll { (iceCream: IceCream) =>
    JsonEncoder[IceCream].encode(iceCream) == toJsonObject(iceCreamLabels, iceCream)
  }

  val rectangleLabels = Mirror.labels[Rectangle]
  val circleLabels = Mirror.labels[Circle]

  property("should have an instance for Shape") = forAll { (shape: Shape) =>
    val jsonObject = shape match
      case r: Rectangle => toJsonObject(rectangleLabels, r)
      case c: Circle => toJsonObject(circleLabels, c)
    JsonEncoder[Shape].encode(shape) == JsonObject(List((Mirror.Sum.label[Shape](shape), jsonObject)))
  }
end JsonEncoderSpecification
