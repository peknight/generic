package com.peknight.generic.priority

trait MidPriorityInstances3[F[_, _, _]] extends LowPriorityInstances3[F]:
  given midPriorityInstances3[A, B, C](using instance: MidPriority[F[A, B, C]]): F[A, B, C] = instance.instance
end MidPriorityInstances3
