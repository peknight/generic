package com.peknight.generic.ops.tuple

private[generic] trait LowPriorityUnion:
  type Aux[L <: Tuple, M <: Tuple, Out0 <: Tuple] = Union[L, M] { type Out = Out0 }
end LowPriorityUnion
