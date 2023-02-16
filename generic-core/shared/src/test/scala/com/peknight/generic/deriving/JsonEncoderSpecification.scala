package com.peknight.generic.deriving

import com.peknight.generic.deriving.JsonValue.*
import com.peknight.generic.deriving.Shape.Circle
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class JsonEncoderSpecification extends AnyFlatSpec:
  "JsonEncoder" should "have an instance for IceCream" in {
    val iceCream = IceCream("Sundae", 1, false)
    assert(JsonEncoder[IceCream].encode(iceCream) == JsonObject(List(
      ("name", JsonString("Sundae")),
      ("numCherries", JsonNumber(1)),
      ("inCone", JsonBoolean(false)))
    ))
  }
  it should "have an instance for Shape" in {
    val shape: Shape = Circle(1.0)
    assert(JsonEncoder[Shape].encode(shape) == JsonObject(List(("Circle", JsonObject(List(("radius", JsonNumber(1))))))))
  }
end JsonEncoderSpecification
