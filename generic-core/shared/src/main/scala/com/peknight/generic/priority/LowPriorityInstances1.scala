package com.peknight.generic.priority

trait LowPriorityInstances1[F[_]]:
  given lowPriorityInstances1[A](using instance: LowPriority[F[A]]): F[A] = instance.instance
end LowPriorityInstances1
