package com.peknight.generic.updater.syntax

import com.peknight.generic.updater.UpdaterT

trait AllSyntax:
  extension[A] (a: A)
    def update[F[_], B](b: B)(using updater: UpdaterT[F, A, B]): F[A] = updater.update(a, b)
  end extension
end AllSyntax

object AllSyntax extends AllSyntax
