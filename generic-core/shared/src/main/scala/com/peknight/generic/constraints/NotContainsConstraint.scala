package com.peknight.generic.constraints

/**
 * Type class witnessing that L doesn't contain elements of type U
 */
private[generic] trait NotContainsConstraint[L <: Tuple, U] extends Serializable

private[generic] object NotContainsConstraint:
  def apply[L <: Tuple, U](using ncc: NotContainsConstraint[L, U]): NotContainsConstraint[L, U] = ncc

  type NotContains[U] = {
    type λ[L <: Tuple] = NotContainsConstraint[L, U]
  }

  given [U]: NotContainsConstraint[EmptyTuple, U] = new NotContainsConstraint[EmptyTuple, U] {}

  given [H, T <: Tuple, U](using nc: NotContainsConstraint[T, U], neq: U =:!= H): NotContainsConstraint[H *: T, U] =
    new NotContainsConstraint[H *: T, U] {}
end NotContainsConstraint
