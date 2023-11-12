package com.peknight.generic.migration

import cats.data.Kleisli
import com.peknight.generic.migration.instances.MigrationInstances

trait Migration[F[_], -A, B]:
  def migrate(a: A): F[B]
  def kleisli: Kleisli[F, A, B] = Kleisli(migrate)
end Migration
object Migration extends MigrationInstances:
  def apply[F[_], A, B](f: A => F[B]): Migration[F, A, B] = f(_)
end Migration
