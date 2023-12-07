package com.peknight.generic.priority

trait HighPriorityInstances2[F[_, _]] extends MidPriorityInstances2[F]:
  given highPriorityInstances2[A, B](using instance: HighPriority[F[A, B]]): F[A, B] = instance.instance
end HighPriorityInstances2
