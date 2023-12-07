package com.peknight.generic.priority

trait LowPriorityInstancesF4[T[_[_], _, _, _, _]]:
  given lowPriorityInstancesF4[F[_], A, B, C, D](using instance: LowPriority[T[F, A, B, C, D]]): T[F, A, B, C, D] =
    instance.instance
end LowPriorityInstancesF4
