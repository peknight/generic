package com.peknight.generic.dependent

/** Dependent nullary function type. */
trait DepFn0:
  type Out
  def apply(): Out
end DepFn0
