package com.peknight.generic.doobie.syntax

import com.peknight.generic.doobie.ops.PutOps
import com.peknight.generic.mapper.Migration
import doobie.Put
import org.tpolecat.typename.TypeName

trait PutSyntax:
  extension [A] (put: Put[A])
    def tmigrate[B](using Migration[B, A], TypeName[B]): Put[B] = PutOps.tmigrate(put)
    def migrate[B](using Migration[B, A]): Put[B] = PutOps.migrate(put)
  end extension
end PutSyntax
object PutSyntax extends PutSyntax
