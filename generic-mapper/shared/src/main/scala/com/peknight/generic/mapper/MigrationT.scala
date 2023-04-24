package com.peknight.generic.mapper

import cats.data.Kleisli
import com.peknight.generic.mapper.instances.migration.AllInstances

trait MigrationT[F[_], -A, B]:
  def migrate(a: A): F[B]
  def kleisli: Kleisli[F, A, B] = Kleisli(migrate)

end MigrationT
object MigrationT extends AllInstances:
  def apply[F[_], A, B](f: A => F[B]): MigrationT[F, A, B] = f(_)
end MigrationT

