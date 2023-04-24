package com.peknight.generic.mapper.instances.bidirectional

import com.peknight.generic.constraints.=:!=
import com.peknight.generic.mapper.BidirectionalT

trait BidirectionalInstances:
  given [F[_], A, B] (using bidirectional: BidirectionalT[F, B, A], neq: A =:!= B): BidirectionalT[F, A, B] with
    def to(a: A): F[B] = bidirectional.from(a)
    def from(b: B): F[A] = bidirectional.to(b)
  end given

end BidirectionalInstances
object BidirectionalInstances extends BidirectionalInstances
