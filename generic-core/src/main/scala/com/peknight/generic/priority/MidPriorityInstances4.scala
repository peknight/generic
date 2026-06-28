package com.peknight.generic.priority

trait MidPriorityInstances4[F[_, _, _, _]] extends LowPriorityInstances4[F]:
  given midPriorityInstances4[A, B, C, D](using instance: MidPriority[F[A, B, C, D]]): F[A, B, C, D] = instance.instance
end MidPriorityInstances4
