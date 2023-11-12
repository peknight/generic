package com.peknight.generic.scalacheck

import com.peknight.generic.scalacheck.JsonValue.JsonObject

trait JsonObjectEncoder[A] extends JsonEncoder[A]:
  def encode(value: A): JsonObject
end JsonObjectEncoder

object JsonObjectEncoder:
  def createObjectEncoder[A](func: A => JsonObject): JsonObjectEncoder[A] = func(_)
end JsonObjectEncoder
