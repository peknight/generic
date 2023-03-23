package com.peknight.generic

import cats.Id

package object migration:
  type Migration[-A, B] = MigrationT[Id, A, B]
end migration
