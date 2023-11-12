package com.peknight.generic.migration.instances

import com.peknight.generic.migration.{Isomorphism, Migration}

trait MigrationIsomorphismInstances:
  given [F[_], A, B](using iso: Isomorphism[F, A, B]): Migration[F, A, B] with
    def migrate(a: A): F[B] = iso.to(a)
  end given
end MigrationIsomorphismInstances
