package com.peknight.generic.priority

trait MidPriorityInstances2[F[_, _]] extends LowPriorityInstances2[F]:
  given midPriorityInstances2[A, B](using instance: MidPriority[F[A, B]]): F[A, B] = instance.instance
end MidPriorityInstances2
