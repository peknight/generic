package com.peknight.generic.monocle

import com.peknight.generic.monocle.instances.setter.given
import com.peknight.generic.scalacheck.instances.all.given
import monocle.Setter
import org.scalacheck.Prop.forAll
import org.scalacheck.Properties

class SetterSpecification extends Properties("Setter"):

  case class IceCream(name: String, numCherries: Int, inCone: Boolean)

  extension [A] (a: A)
    def replace[B](b: B)(using setter: Setter[A, B]): A = setter.replace(b)(a)
  end extension

  property("Update field: update IceCream's name") =
    forAll((iceCream: IceCream, name: String) => {
      val iceCream2 = iceCream.replace(name)
      iceCream2.name == name && iceCream2.numCherries == iceCream.numCherries && iceCream2.inCone == iceCream.inCone
    })

  property("Update field: update IceCream's numCherries") =
    forAll((iceCream: IceCream, numCherries: Int) => {
      val iceCream2 = iceCream.replace(numCherries)
      iceCream2.name == iceCream.name && iceCream2.numCherries == numCherries && iceCream2.inCone == iceCream.inCone
    })

  property("Update field: update IceCream's inCone") =
    forAll((iceCream: IceCream, inCone: Boolean) => {
      val iceCream2 = iceCream.replace(inCone)
      iceCream2.name == iceCream.name && iceCream2.numCherries == iceCream.numCherries && iceCream2.inCone == inCone
    })
