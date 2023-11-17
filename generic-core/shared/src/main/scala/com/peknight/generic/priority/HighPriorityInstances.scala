package com.peknight.generic.priority

trait HighPriorityInstances extends MidPriorityInstances:
  given highPriorityInstance[A](using high: HighPriority[A]): A = high.instance
end HighPriorityInstances
