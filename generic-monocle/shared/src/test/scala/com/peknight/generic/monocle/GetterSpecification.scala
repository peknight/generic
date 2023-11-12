package com.peknight.generic.monocle

import com.peknight.generic.monocle.instances.getter.given
import com.peknight.generic.scalacheck.instances.all.given
import monocle.Getter
import org.scalacheck.Prop.forAll
import org.scalacheck.Properties

class GetterSpecification extends Properties("Getter"):

  case class IceCream(name: String, numCherries: Int, inCone: Boolean)

  extension [A] (a: A)
    def get[B](using getter: Getter[A, B]): B = getter.get(a)
  end extension

  property("Select field: IceCream's name") =
    forAll((iceCream: IceCream) => {
      iceCream.get[String] == iceCream.name
    })

  property("Select field: IceCream's numCherries") =
    forAll((iceCream: IceCream) => {
      iceCream.get[Int] == iceCream.numCherries
    })

  property("Select field: IceCream's inCone") =
    forAll((iceCream: IceCream) => {
      iceCream.get[Boolean] == iceCream.inCone
    })
