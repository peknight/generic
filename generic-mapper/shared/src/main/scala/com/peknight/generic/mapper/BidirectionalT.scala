package com.peknight.generic.mapper

import com.peknight.generic.mapper.instances.bidirectional.AllInstances

trait BidirectionalT[F[_], A, B]:
  def to(a: A): F[B]
  def from(b: B): F[A]
end BidirectionalT

object BidirectionalT extends AllInstances:
  def apply[F[_], A, B](f: A => F[B])(g: B => F[A]): BidirectionalT[F, A, B] = new BidirectionalT[F, A, B]:
    def to(a: A): F[B] = f(a)
    def from(b: B): F[A] = g(b)
end BidirectionalT