package com.peknight.generic.priority

trait LowPriorityInstancesF3[T[_[_], _, _, _]]:
  given lowPriorityInstancesF3[F[_], A, B, C](using instance: LowPriority[T[F, A, B, C]]): T[F, A, B, C] =
    instance.instance
end LowPriorityInstancesF3
