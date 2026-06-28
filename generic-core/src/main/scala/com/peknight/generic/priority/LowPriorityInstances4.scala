package com.peknight.generic.priority

trait LowPriorityInstances4[F[_, _, _, _]]:
  given lowPriorityInstances4[A, B, C, D](using instance: LowPriority[F[A, B, C, D]]): F[A, B, C, D] = instance.instance
end LowPriorityInstances4
