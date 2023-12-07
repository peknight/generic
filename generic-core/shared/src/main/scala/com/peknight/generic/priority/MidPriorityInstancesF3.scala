package com.peknight.generic.priority

trait MidPriorityInstancesF3[T[_[_], _, _, _]] extends LowPriorityInstancesF3[T]:
  given midPriorityInstancesF3[F[_], A, B, C](using instance: MidPriority[T[F, A, B, C]]): T[F, A, B, C] =
    instance.instance
end MidPriorityInstancesF3
