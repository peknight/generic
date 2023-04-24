package com.peknight.generic.doobie.instances

import com.peknight.generic.mapper.Migration
import doobie.Get

trait GetInstances2:
  given [A, B] (using get: Get[A], migration: Migration[A, B]): Get[B] = get.map(migration.migrate)
end GetInstances2

