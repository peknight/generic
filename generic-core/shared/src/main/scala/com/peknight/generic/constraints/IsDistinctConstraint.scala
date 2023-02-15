package com.peknight.generic.constraints

/**
 * Type class witnessing that all elements of L have distinct types
 */
private[generic] trait IsDistinctConstraint[L <: Tuple] extends Serializable

private[generic] object IsDistinctConstraint:
  def apply[L <: Tuple](using idc: IsDistinctConstraint[L]): IsDistinctConstraint[L] = idc

  given IsDistinctConstraint[EmptyTuple] = new IsDistinctConstraint[EmptyTuple] {}

  given [H, T <: Tuple](using d: IsDistinctConstraint[T], nc: NotContainsConstraint[T, H]): IsDistinctConstraint[H *: T] =
    new IsDistinctConstraint[H *: T] {}

end IsDistinctConstraint
