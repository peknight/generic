package com.peknight.generic.doobie

package object instances:
  object all extends GetInstances with PutInstances with MetaInstances with ReadInstances with WriteInstances
  object get extends GetInstances
  object put extends PutInstances
  object meta extends MetaInstances
  object read extends ReadInstances
  object write extends WriteInstances
end instances
