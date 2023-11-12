package com.peknight.generic.migration.instances

import cats.Id
import com.peknight.generic.migration.id.Isomorphism

trait IsomorphismIdInstances:
  given [A]: Isomorphism[A, A] with
    def to(a: A): Id[A] = a
    def from(b: A): Id[A] = b
  end given
end IsomorphismIdInstances
object IsomorphismIdInstances extends IsomorphismIdInstances
