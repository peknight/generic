package com.peknight.generic.monocle.instances

import com.peknight.generic.Mirror
import com.peknight.generic.tuple.ops.Remove
import monocle.Setter

trait SetterInstances:
  given [A <: Product, Repr <: Tuple, B, Rest <: Tuple](using mirror: Mirror.Product.Aux[A, Repr],
                                                        remove: Remove.Aux[Repr, B, (B, Rest)]): Setter[A, B] with
    def modify(f: B => B): A => A = (a: A) =>
      val (b, rest) = remove.apply(Tuple.fromProductTyped(a))
      mirror.fromProduct(remove.reinsert((f(b), rest)))
    def replace(b: B): A => A = modify(_ => b)
  end given
end SetterInstances
object SetterInstances extends SetterInstances
