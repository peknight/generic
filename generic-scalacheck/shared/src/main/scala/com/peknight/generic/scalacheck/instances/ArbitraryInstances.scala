package com.peknight.generic.scalacheck.instances

import com.peknight.cats.instances.scalacheck.arbitrary.given
import com.peknight.generic.deriving.Generic
import org.scalacheck.{Gen, Arbitrary}

trait ArbitraryInstances:

  given productInstance[A](using generic: Generic.Product[Arbitrary, A]): Arbitrary[A] = generic.fromInstances
  given sumInstance[A](using generic: Generic.Sum[Arbitrary, A]): Arbitrary[A] =
    Arbitrary(Gen.choose(0, generic.size - 1).flatMap(i => generic.instance(i).arbitrary))
end ArbitraryInstances
object ArbitraryInstances extends ArbitraryInstances

