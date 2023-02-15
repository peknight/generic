package com.peknight.generic.scalacheck.instances

import com.peknight.cats.instances.scalacheck.arbitrary.given
import com.peknight.generic.deriving.Generic
import org.scalacheck.{Gen, Arbitrary}

trait ArbitraryInstances:
  inline given [A](using generic: Generic[Arbitrary, A]): Arbitrary[A] = generic.derive(
    gen ?=> gen.fromInstances,
    gen ?=> Arbitrary { Gen.choose(0, gen.instances.size - 1).flatMap(i => gen.ordinal(i).arbitrary) }
  )
end ArbitraryInstances
object ArbitraryInstances extends ArbitraryInstances

