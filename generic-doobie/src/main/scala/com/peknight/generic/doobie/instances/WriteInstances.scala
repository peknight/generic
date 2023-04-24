package com.peknight.generic.doobie.instances

import com.peknight.generic.mapper.Migration
import doobie.Write

trait WriteInstances:
  given [A, B] (using write: Write[B], migration: Migration[A, B]): Write[A] = write.contramap[A](migration.migrate)
end WriteInstances
object WriteInstances extends WriteInstances
