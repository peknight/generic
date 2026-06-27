package com.peknight.generic.tuple.ops

private[generic] trait Union2:
  type Aux[L <: Tuple, M <: Tuple, Out0 <: Tuple] = Union[L, M] { type Out = Out0 }
end Union2

