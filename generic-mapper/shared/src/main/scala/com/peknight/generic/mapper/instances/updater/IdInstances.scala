package com.peknight.generic.mapper.instances.updater

import cats.Id
import com.peknight.generic.deriving.Mirror
import com.peknight.generic.mapper.Updater
import com.peknight.generic.tuple.ops.Remove

trait IdInstances:

  given [A]: Updater[A, A] with
    def update(a: A, b: A): Id[A] = b
  end given

  given [A <: Product, Repr <: Tuple, B, Rest <: Tuple](using mirror: Mirror.Product.Aux[A, Repr],
                                                        remove: Remove.Aux[Repr, B, (B, Rest)]): Updater[A, B] with
    def update(a: A, b: B): Id[A] =
      val (_, rest) = remove.apply(Tuple.fromProductTyped(a))
      mirror.fromProduct(remove.reinsert((b, rest)))
  end given
end IdInstances

object IdInstances extends IdInstances
