package com.peknight.generic.mapper.syntax.selector

import com.peknight.generic.mapper.SelectorT

trait AllSyntax:
  extension [A] (a: A)
    def select[F[_], B](using selector: SelectorT[F, A, B]): F[B] = selector.select(a)
  end extension
end AllSyntax

object AllSyntax extends AllSyntax
