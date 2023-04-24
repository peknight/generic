package com.peknight.generic.doobie.syntax

import com.peknight.generic.doobie.ops.MetaOps
import com.peknight.generic.mapper.Migration
import doobie.Meta
import org.tpolecat.typename.TypeName

trait MetaSyntax:
  extension [A] (meta: Meta[A])
    def tmigrate[B](using Migration[A, B], Migration[B, A], TypeName[B]): Meta[B] = MetaOps.tmigrate(meta)
    def migrate[B](using Migration[A, B], Migration[B, A]): Meta[B] = MetaOps.migrate(meta)
  end extension
end MetaSyntax
object MetaSyntax extends MetaSyntax
