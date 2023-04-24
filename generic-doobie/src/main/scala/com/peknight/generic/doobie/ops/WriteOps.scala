package com.peknight.generic.doobie.ops

import com.peknight.generic.mapper.Migration
import doobie.Write

object WriteOps:
  def migrate[A, B](write: Write[A])(using migration: Migration[B, A]): Write[B] =
    write.contramap(migration.migrate)
end WriteOps
