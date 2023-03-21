package com.peknight.generic.updater

import com.peknight.generic.updater.instances.IdInstances

trait UpdaterT[F[_], A, B]:
  def update(a: A, b: B): F[A]
end UpdaterT
object UpdaterT extends IdInstances
