package com.peknight.generic.migration

import cats.Id

package object id:
  type Isomorphism[A, B] = com.peknight.generic.migration.Isomorphism[Id, A, B]
  type Migration[-A, B] = com.peknight.generic.migration.Migration[Id, A, B]
end id
