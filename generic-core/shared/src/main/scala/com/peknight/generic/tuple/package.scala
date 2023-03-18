package com.peknight.generic

package object tuple:
  type Head[A] = A match {case h *: _ => h}
  type Second[A] = A match {case _ *: s *: _ => s}
  type Map[T <: Tuple, F[_]] <: Tuple =
    T match
      case h *: t => F[h] *: Map[t, F]
      case _ => EmptyTuple
  type NonEmptyMap[T <: NonEmptyTuple, F[_]] <: NonEmptyTuple =
    T match
      case h *: t => F[h] *: Map[t, F]
  type Reverse[T <: Tuple] <: Tuple =
    T match
      case h *: t => Tuple.Append[Reverse[t], h]
      case _ => EmptyTuple
  type Typed[T <: Tuple, A] = T match
    case A *: t => Typed[t, A]
    case EmptyTuple => DummyImplicit
  type LabelledValue[A] = (String, A)
  type LabelledTuple[T <: Tuple] <: Tuple =
    T match
      case h *: t => LabelledValue[h] *: LabelledTuple[t]
      case _ => EmptyTuple
  type LabelTuple[T <: Tuple] <: Tuple =
    T match
      case _ *: t => String *: LabelTuple[t]
      case _ => EmptyTuple
end tuple
