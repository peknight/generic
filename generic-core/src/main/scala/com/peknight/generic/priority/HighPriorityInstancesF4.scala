package com.peknight.generic.priority

trait HighPriorityInstancesF4[T[_[_], _, _, _, _]] extends MidPriorityInstancesF4[T]:
  given highPriorityInstancesF4[F[_], A, B, C, D](using instance: HighPriority[T[F, A, B, C, D]]): T[F, A, B, C, D] =
    instance.instance
end HighPriorityInstancesF4
