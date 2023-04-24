package com.peknight.generic.mapper.instances.bidirectional

import com.peknight.generic.constraints.=:!=
import com.peknight.generic.mapper.BidirectionalT

trait BidirectionalInstances:
  given [F[_], A, B] (using bidirectional: BidirectionalT[F, B, A], neq: A =:!= B): BidirectionalT[F, A, B] =
    BidirectionalT(bidirectional.from, bidirectional.to)
end BidirectionalInstances
object BidirectionalInstances extends BidirectionalInstances
