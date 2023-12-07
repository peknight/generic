package com.peknight.generic.priority

trait MidPriorityInstancesF1[T[_[_], _]] extends LowPriorityInstancesF1[T]:
  given midPriorityInstancesF1[F[_], A](using instance: MidPriority[T[F, A]]): T[F, A] = instance.instance
end MidPriorityInstancesF1
