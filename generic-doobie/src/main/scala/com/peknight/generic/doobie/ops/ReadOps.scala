package com.peknight.generic.doobie.ops

import com.peknight.generic.mapper.Migration
import doobie.Read

object ReadOps:
  def migrate[A, B](read: Read[A])(using migration: Migration[A, B]): Read[B] =
    read.map(migration.migrate)
end ReadOps
