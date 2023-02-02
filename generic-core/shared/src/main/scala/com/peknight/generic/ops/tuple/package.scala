package com.peknight.generic.ops

package object tuple:

  type SecondElem[A] = A match { case _ *: s *: _ => s }

  type LiftedTuple[F[_], T <: Tuple] <: Tuple =
    T match
      case h *: t => F[h] *: LiftedTuple[F, t]
      case _ => EmptyTuple

  type TypedTuple[T <: Tuple, A] = T match
    case A *: t => TypedTuple[t, A]
    case EmptyTuple => DummyImplicit

  type Reverse[T <: Tuple] <: Tuple =
    T match
      case h *: t => Tuple.Append[Reverse[t], h]
      case _ => EmptyTuple

end tuple
