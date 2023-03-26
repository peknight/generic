package com.peknight.generic.mapper.instances.selector

import cats.Id
import com.peknight.generic.deriving.Mirror
import com.peknight.generic.mapper.Selector
import com.peknight.generic.tuple.ops.Selector as TupleSelector

trait IdInstances:
  given [A]: Selector[A, A] with
    def select(a: A): Id[A] = a
  end given

  given [A <: Product, B, ARepr <: Tuple](using mirror: Mirror.Product.Aux[A, ARepr], selector: TupleSelector[ARepr, B])
  : Selector[A, B] with
    def select(a: A): Id[B] = selector(Tuple.fromProductTyped(a))
  end given
end IdInstances
object IdInstances extends IdInstances
