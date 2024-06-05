package com.peknight.generic.migration

import cats.Applicative
import cats.syntax.applicative.*
import com.peknight.generic.constraints.=:!=

trait Isomorphism[F[_], A, B]:
  def to(a: A): F[B]
  def from(b: B): F[A]
end Isomorphism
object Isomorphism:
  private case class Iso[F[_], A, B](f: A => F[B], g: B => F[A]) extends Isomorphism[F, A, B]:
    def to(a: A): F[B] = f(a)
    def from(b: B): F[A] = g(b)
  end Iso
  def apply[F[_], A, B](f: A => F[B])(g: B => F[A]): Isomorphism[F, A, B] = Iso(f, g)

  given [F[_]: Applicative, A]: Isomorphism[F, A, A] with
    def to(a: A): F[A] = a.pure[F]
    def from(b: A): F[A] = b.pure[F]
  end given

  given [F[_], A, B] (using isomorphism: Isomorphism[F, B, A], neq: A =:!= B): Isomorphism[F, A, B] with
    def to(a: A): F[B] = isomorphism.from(a)
    def from(b: B): F[A] = isomorphism.to(b)
  end given
end Isomorphism
