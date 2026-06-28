package com.peknight.generic.scalacheck

package object instances:
  object all extends ArbitraryInstances with ShrinkInstances with CogenInstances
  object arbitrary extends ArbitraryInstances
  object shrink extends ShrinkInstances
  object cogen extends CogenInstances
end instances
