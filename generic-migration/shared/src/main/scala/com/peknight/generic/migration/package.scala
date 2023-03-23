package com.peknight.generic

import cats.data.Kleisli
import cats.Id

package object migration:

  // type MigrationT[F[_], -A, B] = Kleisli[F, A, B]
  type Migration[-A, B] = MigrationT[Id, A, B]

end migration