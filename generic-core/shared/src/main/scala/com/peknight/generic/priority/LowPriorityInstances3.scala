package com.peknight.generic.priority

trait LowPriorityInstances3[F[_, _, _]]:
  given lowPriorityInstances3[A, B, C](using instance: LowPriority[F[A, B, C]]): F[A, B, C] = instance.instance
end LowPriorityInstances3
