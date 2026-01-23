package com.peknight.generic.derivation

import cats.Semigroup
import com.peknight.generic.Generic

trait SemigroupDerivation:
  def derived[A](using instances: => Generic.Product.Instances[Semigroup, A]): Semigroup[A] =
    Semigroup.instance[A] { (a1, a2) =>
      instances.combine(a1, a2) { [T] => (instance: Semigroup[T], value1: T, value2: T) => instance.combine(value1, value2) }
    }
end SemigroupDerivation
object SemigroupDerivation extends SemigroupDerivation
