package com.peknight.generic.priority

trait HighPriorityInstancesF2[T[_[_], _, _]] extends MidPriorityInstancesF2[T]:
  given highPriorityInstancesF2[F[_], A, B](using instance: HighPriority[T[F, A, B]]): T[F, A, B] = instance.instance
end HighPriorityInstancesF2
