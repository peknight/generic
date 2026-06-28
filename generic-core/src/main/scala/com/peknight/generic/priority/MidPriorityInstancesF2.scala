package com.peknight.generic.priority

trait MidPriorityInstancesF2[T[_[_], _, _]] extends LowPriorityInstancesF2[T]:
  given midPriorityInstancesF2[F[_], A, B](using instance: MidPriority[T[F, A, B]]): T[F, A, B] = instance.instance
end MidPriorityInstancesF2
