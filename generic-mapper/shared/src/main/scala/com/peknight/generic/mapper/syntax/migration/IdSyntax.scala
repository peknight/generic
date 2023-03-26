package com.peknight.generic.mapper.syntax.migration

import com.peknight.generic.mapper.Migration

trait IdSyntax:
  extension [A] (a: A)
    def migrateTo[B](using migration: Migration[A, B]): B = migration.migrate(a)
  end extension
end IdSyntax

object IdSyntax extends IdSyntax
