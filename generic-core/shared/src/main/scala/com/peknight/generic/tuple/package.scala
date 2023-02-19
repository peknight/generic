package com.peknight.generic

package object tuple:
  type Head[A] = A match {case h *: _ => h}
  type Second[A] = A match {case _ *: s *: _ => s}
  type Lifted[F[_], T <: Tuple] <: Tuple =
    T match
      case h *: t => F[h] *: Lifted[F, t]
      case _ => EmptyTuple
  type Reverse[T <: Tuple] <: Tuple =
    T match
      case h *: t => Tuple.Append[Reverse[t], h]
      case _ => EmptyTuple
  type Typed[T <: Tuple, A] = T match
    case A *: t => Typed[t, A]
    case EmptyTuple => DummyImplicit
end tuple
