package com.peknight.generic.priority

trait MidPriorityInstances[A] extends LowPriorityInstances[A]:
  given midPriorityInstance(using instance: MidPriority[A]): A = instance.instance
end MidPriorityInstances
