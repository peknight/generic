package com.peknight.generic

import cats.Id

package object updater:
  type Updater[A, B] = UpdaterT[Id, A, B]
end updater
