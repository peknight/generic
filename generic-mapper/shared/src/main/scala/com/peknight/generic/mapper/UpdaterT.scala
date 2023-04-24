package com.peknight.generic.mapper

import com.peknight.generic.mapper.instances.updater.AllInstances

trait UpdaterT[F[_], A, -B]:
  def update(a: A, b: B): F[A]
end UpdaterT
object UpdaterT extends AllInstances:
  def apply[F[_], A, B](f: (A, B) => F[A]): UpdaterT[F, A, B] = f(_, _)
end UpdaterT
