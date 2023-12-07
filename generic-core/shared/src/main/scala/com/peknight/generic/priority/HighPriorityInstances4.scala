package com.peknight.generic.priority

trait HighPriorityInstances4[F[_, _, _, _]] extends MidPriorityInstances4[F]:
  given highPriorityInstances4[A, B, C, D](using instance: HighPriority[F[A, B, C, D]]): F[A, B, C, D] =
    instance.instance
end HighPriorityInstances4
