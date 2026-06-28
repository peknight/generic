package com.peknight.generic.priority

trait LowPriorityInstances2[F[_, _]]:
  given lowPriorityInstances2[A, B](using instance: LowPriority[F[A, B]]): F[A, B] = instance.instance
end LowPriorityInstances2
