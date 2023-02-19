package com.peknight.generic.tuple.ops

private[generic] trait LowPriorityDiff:
  type Aux[L <: Tuple, M <: Tuple, Out0] = Diff[L, M] { type Out = Out0 }

  given [L <: Tuple, H, T <: Tuple, D <: Tuple](using d: Aux[L, T, D]): Aux[L, H *: T, D] =
    new Diff[L, H *: T]:
      type Out = D
      def apply(l: L): Out = d(l)
  end given
end LowPriorityDiff
