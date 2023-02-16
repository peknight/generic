package com.peknight.generic.deriving

import com.peknight.generic.deriving.JsonValue.JsonObject

trait JsonObjectEncoder[A] extends JsonEncoder[A]:
  def encode(value: A): JsonObject
end JsonObjectEncoder

object JsonObjectEncoder:
  def createObjectEncoder[A](func: A => JsonObject): JsonObjectEncoder[A] = func(_)
end JsonObjectEncoder
