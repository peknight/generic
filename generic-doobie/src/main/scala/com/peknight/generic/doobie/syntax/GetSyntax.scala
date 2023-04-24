package com.peknight.generic.doobie.syntax

import com.peknight.generic.doobie.ops.GetOps
import com.peknight.generic.mapper.Migration
import doobie.Get
import org.tpolecat.typename.TypeName

trait GetSyntax:
  extension [A] (get: Get[A])
    def tmigrate[B](using Migration[A, B], TypeName[B]): Get[B] = GetOps.tmigrate(get)
    def migrate[B](using Migration[A, B]): Get[B] = GetOps.migrate(get)
  end extension
end GetSyntax
object GetSyntax extends GetSyntax
