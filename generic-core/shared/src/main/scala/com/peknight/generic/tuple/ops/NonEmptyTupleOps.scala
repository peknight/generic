package com.peknight.generic.tuple.ops

import cats.{Functor, Semigroupal}
import com.peknight.generic.tuple.NonEmptyMap

import scala.Tuple.Last

object NonEmptyTupleOps:
  def mapN[T <: NonEmptyTuple, G[_] : Functor : Semigroupal, Z](tuple: NonEmptyMap[T, G])(f: T => Z): G[Z] =
    type F[A] = A match {case G[x] => x}
    val reversed: NonEmptyTuple = TupleOps._reverse(tuple).asInstanceOf[NonEmptyTuple]
    val gt: G[T] = TupleOps.loop[G](
      reversed.tail,
      Functor[G].map(reversed.head.asInstanceOf[G[F[Last[NonEmptyMap[T, G]]]]])(_ *: EmptyTuple)
    ) {
      [A] => (a: A, acc: G[Tuple]) =>
        Functor[G].map(Semigroupal[G].product[F[A], Tuple](a.asInstanceOf[G[F[A]]], acc)) {
          case (h, t) => h *: t
        }
    }.asInstanceOf[G[T]]
    Functor[G].map(gt)(f)

end NonEmptyTupleOps
