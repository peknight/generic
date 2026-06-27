package com.peknight.generic

import com.peknight.generic.tuple.Map

import scala.compiletime.*
import scala.deriving.Mirror as SMirror

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

  inline def summonSingletons[T <: Tuple]: Map[T, Option] =
    summonOptionTuple[SMirror.ProductOf, T].map[[_] =>> Any]([X] => (x: X) =>
      x.asInstanceOf[Option[SMirror.Product]].filter(_.isInstanceOf[SMirror.Singleton]).map(_.fromProduct(EmptyTuple))
    ).asInstanceOf[Map[T, Option]]

  inline def summonAllSingletons[T <: Tuple](inline typeName: Any): T =
    inline erasedValue[T] match
      case _: EmptyTuple => EmptyTuple.asInstanceOf[T]
      case _: (h *: t) =>
        inline summonInline[SMirror.Of[h]] match
          case m: SMirror.Singleton => (m.fromProduct(EmptyTuple) *: summonAllSingletons[t](typeName)).asInstanceOf[T]
          case m: SMirror =>
            error("Enum " + codeOf(typeName) + " contains non singleton case " + codeOf(constValue[m.MirroredLabel]))
  end summonAllSingletons
end compiletime
