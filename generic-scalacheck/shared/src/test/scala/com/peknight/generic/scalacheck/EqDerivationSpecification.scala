package com.peknight.generic.scalacheck

import cats.syntax.eq.*
import cats.{Eq, Semigroup}
import com.peknight.generic.derivation.EqDerivation
import com.peknight.generic.scalacheck.instances.all.given
import org.scalacheck.Prop.forAll
import org.scalacheck.Properties

class EqDerivationSpecification extends Properties("EqDerivation"):

  property("should have an instance for IceCream") = forAll { (iceCream1: IceCream, iceCream2: IceCream) =>
    given Eq[IceCream] = EqDerivation.derived[IceCream]
    iceCream1 === iceCream1 && iceCream2 === iceCream2 && (iceCream1 === iceCream2) === (iceCream1 == iceCream2)
  }
end EqDerivationSpecification
