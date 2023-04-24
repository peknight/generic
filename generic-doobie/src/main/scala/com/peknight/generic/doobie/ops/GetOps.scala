package com.peknight.generic.doobie.ops

import com.peknight.generic.mapper.Migration
import doobie.Get
import org.tpolecat.typename.TypeName

object GetOps:
  def tmigrate[A, B](get: Get[A])(using migration: Migration[A, B], ev: TypeName[B]): Get[B] =
    get.tmap(migration.migrate)
  def migrate[A, B](get: Get[A])(using migration: Migration[A, B]): Get[B] =
    get.map(migration.migrate)
end GetOps
