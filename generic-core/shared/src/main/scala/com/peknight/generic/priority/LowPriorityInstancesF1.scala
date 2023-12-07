package com.peknight.generic.priority

trait LowPriorityInstancesF1[T[_[_], _]]:
  given lowPriorityInstancesF1[F[_], A](using instance: LowPriority[T[F, A]]): T[F, A] = instance.instance
end LowPriorityInstancesF1
