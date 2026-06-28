package com.peknight.generic.instances

import cats.Show
import com.peknight.generic.Generic
import com.peknight.generic.derivation.ShowDerivation

trait ShowInstances:
  given derivedShow[A](using Generic.Instances[Show, A]): Show[A] = ShowDerivation.derived[A]
end ShowInstances
object ShowInstances extends ShowInstances
