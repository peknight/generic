package com.peknight.generic.mapper

import cats.Id

object Bidirectional:
  def apply[A, B](f: A => B)(g: B => A): Bidirectional[A, B] = new BidirectionalT[Id, A, B]:
    def to(a: A): Id[B] = f(a)
    def from(b: B): Id[A] = g(b)
end Bidirectional

