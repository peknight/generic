package com.peknight.generic.migration.instances

import com.peknight.generic.constraints.=:!=
import com.peknight.generic.migration.Isomorphism

trait IsomorphismInstances extends IsomorphismIdInstances:
  given [F[_], A, B] (using isomorphism: Isomorphism[F, B, A], neq: A =:!= B): Isomorphism[F, A, B] with
    def to(a: A): F[B] = isomorphism.from(a)
    def from(b: B): F[A] = isomorphism.to(b)
  end given
end IsomorphismInstances
object IsomorphismInstances extends IsomorphismInstances
