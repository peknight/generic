package com.peknight.generic.doobie.ops

import com.peknight.generic.mapper.Migration
import doobie.Meta
import org.tpolecat.typename.TypeName

object MetaOps:
  def tmigrate[A, B](meta: Meta[A])(using aToB: Migration[A, B], bToA: Migration[B, A], ev: TypeName[B]): Meta[B] =
    meta.timap(aToB.migrate)(bToA.migrate)
  def migrate[A, B](meta: Meta[A])(using aToB: Migration[A, B], bToA: Migration[B, A]): Meta[B] =
    meta.imap(aToB.migrate)(bToA.migrate)
end MetaOps
