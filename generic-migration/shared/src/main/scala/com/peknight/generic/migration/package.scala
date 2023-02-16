package com.peknight.generic

import cats.data.Kleisli
import cats.{Id, Monoid}

package object migration:

  type MigrationT[F[_], -A, B] = Kleisli[F, A, B]
  type Migration[-A, B] = Kleisli[Id, A, B]

end migration