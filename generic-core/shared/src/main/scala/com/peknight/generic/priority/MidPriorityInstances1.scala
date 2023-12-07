package com.peknight.generic.priority

trait MidPriorityInstances1[F[_]] extends LowPriorityInstances1[F]:
  given midPriorityInstances1[A](using instance: MidPriority[F[A]]): F[A] = instance.instance
end MidPriorityInstances1
