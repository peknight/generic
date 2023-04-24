package com.peknight.generic.doobie.ops

import com.peknight.generic.mapper.Migration
import doobie.Put
import org.tpolecat.typename.TypeName

object PutOps:
  def tmigrate[A, B](put: Put[A])(using migration: Migration[B, A], ev: TypeName[B]): Put[B] =
    put.tcontramap(migration.migrate)
  def migrate[A, B](put: Put[A])(using migration: Migration[B, A]): Put[B] =
    put.contramap(migration.migrate)
end PutOps
