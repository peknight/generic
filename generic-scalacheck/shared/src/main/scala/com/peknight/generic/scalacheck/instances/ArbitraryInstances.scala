package com.peknight.generic.scalacheck.instances

import com.peknight.cats.scalacheck.instances.arbitrary.given
import com.peknight.generic.Generic
import org.scalacheck.{Arbitrary, Gen}

trait ArbitraryInstances:

  given derived[A](using instances: => Generic.Instances[Arbitrary, A]): Arbitrary[A] = instances.derive(
    inst ?=> inst.fromInstances,
    inst ?=> Arbitrary(Gen.choose(0, inst.size - 1).flatMap(i => inst.instance(i).arbitrary))
  )

end ArbitraryInstances
object ArbitraryInstances extends ArbitraryInstances

