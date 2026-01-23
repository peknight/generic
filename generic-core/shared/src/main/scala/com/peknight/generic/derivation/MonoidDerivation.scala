package com.peknight.generic.derivation

import cats.{Id, Monoid}
import com.peknight.generic.Generic

trait MonoidDerivation:
  def derived[A](using instances: => Generic.Product.Instances[Monoid, A]): Monoid[A] =
    Monoid.instance[A](
      instances.construct[Id]([T] => (monoid: Monoid[T]) => monoid.empty),
      (a1, a2) => instances.combine(a1, a2) { [T] => (instance: Monoid[T], value1: T, value2: T) => instance.combine(value1, value2) }
    )
end MonoidDerivation
object MonoidDerivation extends MonoidDerivation
