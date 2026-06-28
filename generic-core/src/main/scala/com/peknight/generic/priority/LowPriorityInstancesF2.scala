package com.peknight.generic.priority

trait LowPriorityInstancesF2[T[_[_], _, _]]:
  given lowPriorityInstancesF2[F[_], A, B](using instance: LowPriority[T[F, A, B]]): T[F, A, B] = instance.instance
end LowPriorityInstancesF2
