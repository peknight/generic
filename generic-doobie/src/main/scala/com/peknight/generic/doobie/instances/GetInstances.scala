package com.peknight.generic.doobie.instances

import com.peknight.generic.mapper.Migration
import doobie.Get
import org.tpolecat.typename.TypeName

trait GetInstances extends GetInstances2:
  given [A, B] (using get: Get[A], migration: Migration[A, B], ev: TypeName[B]): Get[B] = get.tmap(migration.migrate)
end GetInstances
object GetInstances extends GetInstances
