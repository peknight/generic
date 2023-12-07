package com.peknight.generic.priority

trait HighPriorityInstancesF3[T[_[_], _, _, _]] extends MidPriorityInstancesF3[T]:
  given highPriorityInstancesF3[F[_], A, B, C](using instance: HighPriority[T[F, A, B, C]]): T[F, A, B, C] =
    instance.instance
end HighPriorityInstancesF3
