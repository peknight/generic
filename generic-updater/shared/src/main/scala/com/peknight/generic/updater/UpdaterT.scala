package com.peknight.generic.updater

trait UpdaterT[F[_], A, B]:
  def update(a: A, b: B): F[A]
end UpdaterT
