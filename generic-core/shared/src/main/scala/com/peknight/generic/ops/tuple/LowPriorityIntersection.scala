package com.peknight.generic.ops.tuple

private[generic] trait LowPriorityIntersection:
  type Aux[L <: Tuple, M <: Tuple, Out0 <: Tuple] = Intersection[L, M] { type Out = Out0 }
end LowPriorityIntersection

