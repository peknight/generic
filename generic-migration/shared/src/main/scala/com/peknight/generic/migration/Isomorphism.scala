package com.peknight.generic.migration

import com.peknight.generic.migration.instances.IsomorphismInstances

trait Isomorphism[F[_], A, B]:
  def to(a: A): F[B]
  def from(b: B): F[A]
end Isomorphism
object Isomorphism extends IsomorphismInstances:
  private[this] case class Iso[F[_], A, B](f: A => F[B], g: B => F[A]) extends Isomorphism[F, A, B]:
    def to(a: A): F[B] = f(a)
    def from(b: B): F[A] = g(b)
  end Iso
  def apply[F[_], A, B](f: A => F[B])(g: B => F[A]): Isomorphism[F, A, B] = Iso(f, g)
end Isomorphism
