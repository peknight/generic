package com.peknight.generic

package object constraint:
  private[constraint] def unexpected: Nothing = sys.error("Unexpected invocation")
end constraint
