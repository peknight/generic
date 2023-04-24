package com.peknight.generic.mapper

object Selector:
  def apply[A, B](f: A => B): Selector[A, B] = f(_)
end Selector
