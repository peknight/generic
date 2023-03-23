package com.peknight.generic.migration

import cats.data.Kleisli
import com.peknight.generic.migration.instances.AllInstances

trait MigrationT[F[_], -A, B]:
  def run(a: A): F[B]
  def kleisli: Kleisli[F, A, B] = Kleisli(run)

end MigrationT
object MigrationT extends AllInstances
