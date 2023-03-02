package com.peknight.generic.tuple.ops

private[generic] trait Prepend2 extends Prepend3:
  given [P <: Tuple, S <: EmptyTuple]: Aux[P, S, P] =
    new Prepend[P, S]:
      type Out = P
      def apply(prefix: P, suffix: S): P = prefix
  end given
end Prepend2

