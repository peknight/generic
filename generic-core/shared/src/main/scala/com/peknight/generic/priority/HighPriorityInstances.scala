package com.peknight.generic.priority

trait HighPriorityInstances[A] extends MidPriorityInstances[A]:
  given highPriorityInstance(using instance: HighPriority[A]): A = instance.instance
end HighPriorityInstances
