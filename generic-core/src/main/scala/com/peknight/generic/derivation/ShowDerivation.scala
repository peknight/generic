package com.peknight.generic.derivation

import cats.Show
import com.peknight.generic.Generic

trait ShowDerivation:
  def derived[A](using instances: => Generic.Instances[Show, A]): Show[A] =
    instances.derive(
      inst ?=> derivedProduct[A](using inst),
      inst ?=> derivedSum[A](using inst),
    )
  end derived

  private def derivedProduct[A](using instances: => Generic.Product.Instances[Show, A]): Show[A] =
    type LiftString[_] = String
    Show.show[A](a => instances.mapWithLabel[LiftString](a)(
        [T] => (show, t, label) => s"$label=${show.show(t)}".asInstanceOf
    ).toList.asInstanceOf[List[String]].mkString(s"${instances.label}(", ",", ")"))

  private def derivedSum[A](using instances: => Generic.Sum.Instances[Show, A]): Show[A] =
    Show.show[A](a => instances.instance(a).show(a))
end ShowDerivation
object ShowDerivation extends ShowDerivation
