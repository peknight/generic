package com.peknight.generic

import com.peknight.generic.tuple.Map

import scala.compiletime.{erasedValue, summonFrom}

package object compiletime:
  inline def summonOption[A]: Option[A] =
    summonFrom[Option[A]] {
      case a @ given A => Some(a)
      case _ => None
    }

  inline def summonOptionTuple[F[_], T <: Tuple]: Map[T, [X] =>> Option[F[X]]] =
    inline erasedValue[T] match
      case _: EmptyTuple => EmptyTuple.asInstanceOf[Map[T, [X] =>> Option[F[X]]]]
      case _: (h *: t) => (summonOption[F[h]] *: summonOptionTuple[F, t]).asInstanceOf[Map[T, [X] =>> Option[F[X]]]]
end compiletime
