package com.peknight.generic.priority

trait HighPriorityInstances1[F[_]] extends MidPriorityInstances1[F]:
  given highPriorityInstances1[A](using instance: HighPriority[F[A]]): F[A] = instance.instance
end HighPriorityInstances1
