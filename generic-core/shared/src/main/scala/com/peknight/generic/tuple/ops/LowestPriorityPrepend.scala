package com.peknight.generic.tuple.ops

private[generic] trait LowestPriorityPrepend:
  type Aux[P <: Tuple, S <: Tuple, Out0 <: Tuple] = Prepend[P, S] {type Out = Out0}

  given [PH, PT <: Tuple, S <: Tuple, PtOut <: Tuple](using pt: Aux[PT, S, PtOut]): Aux[PH *: PT, S, PH *: PtOut] =
    new Prepend[PH *: PT, S]:
      type Out = PH *: PtOut
      def apply(prefix: PH *: PT, suffix: S): Out = prefix.head *: pt(prefix.tail, suffix)
  end given
end LowestPriorityPrepend

