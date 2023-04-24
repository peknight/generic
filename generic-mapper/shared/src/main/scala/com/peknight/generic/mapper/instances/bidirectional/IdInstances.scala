package com.peknight.generic.mapper.instances.bidirectional

import cats.Id
import com.peknight.generic.mapper.Bidirectional

trait IdInstances:
  given [A]: Bidirectional[A, A] with
    def to(a: A): Id[A] = a
    def from(b: A): Id[A] = b
  end given
end IdInstances
object IdInstances extends IdInstances