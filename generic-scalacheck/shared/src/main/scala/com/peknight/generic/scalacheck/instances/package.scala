package com.peknight.generic.scalacheck

package object instances:
  object all extends ArbitraryInstances with ShrinkInstances
  object arbitrary extends ArbitraryInstances
  object shrink extends ShrinkInstances
end instances
