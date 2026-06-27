package com.peknight.generic.priority

trait HighPriorityInstancesF1[T[_[_], _]] extends MidPriorityInstancesF1[T]:
  given highPriorityInstancesF1[F[_], A](using instance: HighPriority[T[F, A]]): T[F, A] = instance.instance
end HighPriorityInstancesF1
