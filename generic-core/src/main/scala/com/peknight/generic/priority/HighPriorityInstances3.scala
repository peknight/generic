package com.peknight.generic.priority

trait HighPriorityInstances3[F[_, _, _]] extends MidPriorityInstances3[F]:
  given highPriorityInstances3[A, B, C](using instance: HighPriority[F[A, B, C]]): F[A, B, C] = instance.instance
end HighPriorityInstances3
