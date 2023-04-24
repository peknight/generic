package com.peknight.generic.doobie.instances

import com.peknight.generic.mapper.Migration
import doobie.Read

trait ReadInstances:
  given [A, B] (using read: Read[A], migration: Migration[A, B]): Read[B] = read.map(migration.migrate)
end ReadInstances
object ReadInstances extends ReadInstances
