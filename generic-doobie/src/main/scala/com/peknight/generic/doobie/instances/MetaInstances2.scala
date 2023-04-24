package com.peknight.generic.doobie.instances

import com.peknight.generic.mapper.Bidirectional
import doobie.Meta

trait MetaInstances2:
  given [A, B] (using meta: Meta[A], bidirectional: Bidirectional[A, B]): Meta[B] =
    meta.imap(bidirectional.to)(bidirectional.from)
end MetaInstances2
