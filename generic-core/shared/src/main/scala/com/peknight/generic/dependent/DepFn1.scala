package com.peknight.generic.dependent

/** Dependent unary function type. */
trait DepFn1[T]:
  type Out
  def apply(t: T): Out
end DepFn1
