package com.peknight.generic.monocle.instances

import com.peknight.generic.Mirror
import com.peknight.generic.tuple.ops.Selector
import monocle.Getter

trait GetterInstances:
  given [A <: Product, B, ARepr <: Tuple](using mirror: Mirror.Product.Aux[A, ARepr], selector: Selector[ARepr, B])
  : Getter[A, B] with
    def get(s: A): B = selector(Tuple.fromProductTyped(s))
  end given
end GetterInstances
object GetterInstances extends GetterInstances
