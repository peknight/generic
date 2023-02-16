package com.peknight.generic.migration.syntax

import com.peknight.generic.migration.Migration

trait IdSyntax:
  extension[A] (a: A)
    def migrateTo[B](using migration: Migration[A, B]): B = migration.run(a)
  end extension
end IdSyntax

object IdSyntax extends IdSyntax
