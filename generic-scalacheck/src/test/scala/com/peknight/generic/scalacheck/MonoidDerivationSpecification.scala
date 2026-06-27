package com.peknight.generic.scalacheck

import cats.syntax.eq.*
import cats.{Eq, Monoid, Semigroup}
import com.peknight.generic.derivation.{monoid, semigroup}
import com.peknight.generic.scalacheck.instances.all.given
import org.scalacheck.Prop.forAll
import org.scalacheck.Properties

class MonoidDerivationSpecification extends Properties("MonoidDerivation"):

  given Monoid[Boolean] = Monoid.instance[Boolean](true, _ && _)
  given Eq[IceCream] = Eq.fromUniversalEquals[IceCream]

  property("should have a semigroup instance for IceCream") = forAll { (iceCream1: IceCream, iceCream2: IceCream) =>
    given Semigroup[IceCream] = semigroup.derived[IceCream]
    Semigroup[IceCream].combine(iceCream1, iceCream2) === IceCream(
      s"${iceCream1.name}${iceCream2.name}",
      iceCream1.numCherries + iceCream2.numCherries,
      iceCream1.inCone && iceCream2.inCone
    )
  }

  property("should have a monoid instance for IceCream") = forAll { (iceCream1: IceCream, iceCream2: IceCream) =>
    given Monoid[IceCream] = monoid.derived[IceCream]
    Monoid[IceCream].empty === IceCream("", 0, true) && Monoid[IceCream].combine(iceCream1, iceCream2) === IceCream(
      s"${iceCream1.name}${iceCream2.name}",
      iceCream1.numCherries + iceCream2.numCherries,
      iceCream1.inCone && iceCream2.inCone
    )
  }
end MonoidDerivationSpecification
