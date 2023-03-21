package com.peknight.generic.updater

import com.peknight.generic.scalacheck.instances.all.given
import com.peknight.generic.updater.instances.id.given
import com.peknight.generic.updater.syntax.id.update
import org.scalacheck.Prop.forAll
import org.scalacheck.Properties

class UpdaterSpecification extends Properties("Updater"):

  case class IceCream(name: String, numCherries: Int, inCone: Boolean)

  property("Update field: update IceCream's name") =
    forAll((iceCream: IceCream, name: String) => {
      val iceCream2 = iceCream.update(name)
      iceCream2.name == name && iceCream2.numCherries == iceCream.numCherries && iceCream2.inCone == iceCream.inCone
    })

  property("Update field: update IceCream's numCherries") =
    forAll((iceCream: IceCream, numCherries: Int) => {
      val iceCream2 = iceCream.update(numCherries)
      iceCream2.name == iceCream.name && iceCream2.numCherries == numCherries && iceCream2.inCone == iceCream.inCone
    })

  property("Update field: update IceCream's inCone") =
    forAll((iceCream: IceCream, inCone: Boolean) => {
      val iceCream2 = iceCream.update(inCone)
      iceCream2.name == iceCream.name && iceCream2.numCherries == iceCream.numCherries && iceCream2.inCone == inCone
    })
