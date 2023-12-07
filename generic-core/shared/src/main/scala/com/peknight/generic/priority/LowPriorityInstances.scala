package com.peknight.generic.priority

trait LowPriorityInstances[A]:
  given lowPriorityInstance(using instance: LowPriority[A]): A = instance.instance
end LowPriorityInstances
