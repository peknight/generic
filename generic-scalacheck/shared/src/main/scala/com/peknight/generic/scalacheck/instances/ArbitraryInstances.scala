package com.peknight.generic.scalacheck.instances

import com.peknight.cats.instances.scalacheck.arbitrary.given
import com.peknight.generic.deriving.Generic
import org.scalacheck.{Gen, Arbitrary}

trait ArbitraryInstances:

  //given productInstance[A](using generic: => Generic.Product[Arbitrary, A]): Arbitrary[A] = generic.fromInstances

  //given sumInstance[A](using generic: => Generic.Sum[Arbitrary, A]): Arbitrary[A] =
  //  Arbitrary(Gen.choose(0, generic.size - 1).flatMap(i => generic.instance(i).arbitrary))

  inline given derived[A](using generic: => Generic[Arbitrary, A]): Arbitrary[A] = generic.derive(
    gen ?=> gen.fromInstances,
    gen ?=> Arbitrary(Gen.choose(0, gen.size - 1).flatMap(i => gen.instance(i).arbitrary))
  )

end ArbitraryInstances
object ArbitraryInstances extends ArbitraryInstances

