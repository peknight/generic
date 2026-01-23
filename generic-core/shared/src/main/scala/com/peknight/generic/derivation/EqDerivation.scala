package com.peknight.generic.derivation

import cats.Eq
import com.peknight.generic.Generic
import com.peknight.generic.tuple.syntax.foldLeft

trait EqDerivation:
  def derived[A](using instances: => Generic.Product.Instances[Eq, A]): Eq[A] =
    Eq.instance[A] { (a1, a2) =>
      instances.instances.zip(instances.to(a1).zip(instances.to(a2))).foldLeft[Boolean](true) {
        [E] => (acc: Boolean, e: E) =>
          type U = E match { case (_, (t, _)) => t }
          if acc then
            val (eq, (value1, value2)) = e.asInstanceOf[(Eq[U], (U, U))]
            eq.eqv(value1, value2)
          else
            false
      }
    }
end EqDerivation
object EqDerivation extends EqDerivation
