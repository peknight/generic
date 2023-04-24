package com.peknight.generic.mapper.instances.migration

import com.peknight.generic.mapper.{BidirectionalT, MigrationT}

trait BidirectionalInstances:
  given [F[_], A, B] (using bidirectional: BidirectionalT[F, A, B]): MigrationT[F, A, B] with
    def migrate(a: A): F[B] = bidirectional.to(a)
  end given

end BidirectionalInstances
object BidirectionalInstances extends BidirectionalInstances
