package com.peknight.generic.mapper.syntax.migration

import com.peknight.generic.mapper.MigrationT

trait AllSyntax:
  extension [A] (a: A)
    def migrateTo[F[_], B](using migration: MigrationT[F, A, B]): F[B] = migration.migrate(a)
  end extension
end AllSyntax

object AllSyntax extends AllSyntax
