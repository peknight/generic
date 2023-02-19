package com.peknight.generic.tuple.ops

private[generic] trait LowPriorityPrepend extends LowestPriorityPrepend:
  given [P <: Tuple, S <: EmptyTuple]: Aux[P, S, P] =
    new Prepend[P, S]:
      type Out = P
      def apply(prefix: P, suffix: S): P = prefix
  end given
end LowPriorityPrepend

