package com.peknight.generic.migration.syntax

import com.peknight.generic.migration.MigrationT

trait AllSyntax:
  extension[A] (a: A)
    def migrateTo[F[_], B](using migration: MigrationT[F, A, B]): F[B] = migration.run(a)
  end extension
end AllSyntax

object AllSyntax extends AllSyntax
