package com.peknight.generic.mapper.syntax.selector

import com.peknight.generic.mapper.Selector

trait IdSyntax:
  extension [A] (a: A)
    def select[B](using selector: Selector[A, B]): B = selector.select(a)
  end extension
end IdSyntax

object IdSyntax extends IdSyntax
