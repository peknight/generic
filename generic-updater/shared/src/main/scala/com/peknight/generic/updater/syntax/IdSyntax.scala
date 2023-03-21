package com.peknight.generic.updater.syntax

import com.peknight.generic.updater.Updater

trait IdSyntax:
  extension[A] (a: A)
    def update[B](b: B)(using updater: Updater[A, B]): A = updater.update(a, b)
  end extension
end IdSyntax

object IdSyntax extends IdSyntax
