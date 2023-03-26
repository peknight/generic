package com.peknight.generic.mapper.syntax.updater

import com.peknight.generic.mapper.Updater

trait IdSyntax:
  extension[A] (a: A)
    def update[B](b: B)(using updater: Updater[A, B]): A = updater.update(a, b)
  end extension
end IdSyntax
object IdSyntax extends IdSyntax
