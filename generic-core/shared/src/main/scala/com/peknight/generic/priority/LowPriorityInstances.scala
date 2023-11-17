package com.peknight.generic.priority

trait LowPriorityInstances:
  given lowPriorityInstance[A](using low: LowPriority[A]): A = low.instance
end LowPriorityInstances
