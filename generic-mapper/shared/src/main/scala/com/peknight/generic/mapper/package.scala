package com.peknight.generic

import cats.Id

package object mapper:
  type Migration[-A, B] = MigrationT[Id, A, B]
  type Selector[-A, B] = SelectorT[Id, A, B]
  type Updater[A, B] = UpdaterT[Id, A, B]
end mapper
