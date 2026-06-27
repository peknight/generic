package com.peknight.generic.dependent

/** Dependent binary function type. */
trait DepFn2[T, U]:
  type Out
  def apply(t: T, u: U): Out
end DepFn2
