package com.peknight.generic.doobie.instances

import com.peknight.generic.mapper.Migration
import doobie.Put
import org.tpolecat.typename.TypeName

trait PutInstances extends PutInstances2:
  given [A, B] (using put: Put[B], migration: Migration[A, B], ev: TypeName[A]): Put[A] =
    put.tcontramap[A](migration.migrate)

end PutInstances
object PutInstances extends PutInstances
