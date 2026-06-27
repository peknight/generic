package com.peknight.generic.priority

trait MidPriorityInstancesF4[T[_[_], _, _, _, _]] extends LowPriorityInstancesF4[T]:
  given midPriorityInstancesF4[F[_], A, B, C, D](using instance: MidPriority[T[F, A, B, C, D]]): T[F, A, B, C, D] =
    instance.instance
end MidPriorityInstancesF4
