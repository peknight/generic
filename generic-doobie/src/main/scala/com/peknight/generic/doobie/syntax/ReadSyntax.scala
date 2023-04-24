package com.peknight.generic.doobie.syntax

import com.peknight.generic.doobie.ops.ReadOps
import com.peknight.generic.mapper.Migration
import doobie.Read

trait ReadSyntax:
  extension [A] (read: Read[A])
    def migrate[B](using Migration[A, B]): Read[B] = ReadOps.migrate(read)
  end extension
end ReadSyntax
object ReadSyntax extends ReadSyntax
