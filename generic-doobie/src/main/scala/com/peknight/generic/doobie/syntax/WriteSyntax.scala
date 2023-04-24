package com.peknight.generic.doobie.syntax

import com.peknight.generic.doobie.ops.WriteOps
import com.peknight.generic.mapper.Migration
import doobie.Write

trait WriteSyntax:
  extension [A] (write: Write[A])
    def migrate[B](using Migration[B, A]): Write[B] = WriteOps.migrate(write)
  end extension
end WriteSyntax
object WriteSyntax extends WriteSyntax
