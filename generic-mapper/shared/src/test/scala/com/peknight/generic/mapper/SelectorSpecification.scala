package com.peknight.generic.mapper

import com.peknight.generic.mapper.syntax.selector.id.select
import com.peknight.generic.scalacheck.instances.all.given
import org.scalacheck.Prop.forAll
import org.scalacheck.Properties

class SelectorSpecification extends Properties("Selector"):

  case class IceCream(name: String, numCherries: Int, inCone: Boolean)

  property("Select field: IceCream's name") =
    forAll((iceCream: IceCream) => {
      iceCream.select[String] == iceCream.name
    })

  property("Select field: IceCream's numCherries") =
    forAll((iceCream: IceCream) => {
      iceCream.select[Int] == iceCream.numCherries
    })

  property("Select field: IceCream's inCone") =
    forAll((iceCream: IceCream) => {
      iceCream.select[Boolean] == iceCream.inCone
    })
