package com.peknight

package object generic:
  type Mirror[A] = scala.deriving.Mirror.Of[A]
end generic
