package com.peknight.generic.doobie.instances

import com.peknight.generic.mapper.Bidirectional
import doobie.Meta
import org.tpolecat.typename.TypeName

trait MetaInstances extends MetaInstances2:
  given[A, B](using meta: Meta[A], bidirectional: Bidirectional[A, B], ev: TypeName[B]): Meta[B] =
    meta.timap(bidirectional.to)(bidirectional.from)
end MetaInstances
object MetaInstances extends MetaInstances
