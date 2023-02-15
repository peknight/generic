package com.peknight.generic

package object constraints:
  private[constraints] def unexpected: Nothing = sys.error("Unexpected invocation")
end constraints
