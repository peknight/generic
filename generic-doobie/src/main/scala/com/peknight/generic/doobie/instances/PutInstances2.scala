package com.peknight.generic.doobie.instances

import com.peknight.generic.mapper.Migration
import doobie.Put

trait PutInstances2:
  given [A, B] (using put: Put[B], migration: Migration[A, B]): Put[A] = put.contramap[A](migration.migrate)
end PutInstances2
