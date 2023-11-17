package com.peknight.generic.priority

trait MidPriorityInstances extends LowPriorityInstances:
  given midPriorityInstance[A](using mid: MidPriority[A]): A = mid.instance
end MidPriorityInstances
