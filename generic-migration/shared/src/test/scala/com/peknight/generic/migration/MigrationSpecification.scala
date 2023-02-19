package com.peknight.generic.migration

import com.peknight.generic.migration.instances.id.given
import com.peknight.generic.migration.syntax.id.migrateTo
import com.peknight.generic.scalacheck.instances.all.given
import org.scalacheck.Prop.forAll
import org.scalacheck.Properties

class MigrationSpecification extends Properties("Migration"):

  case class IceCreamV1(name: String, numCherries: Int, inCone: Boolean)

  case class IceCreamV2a(name: String, inCone: Boolean)

  case class IceCreamV2b(name: String, inCone: Boolean, numCherries: Int)

  case class IceCreamV2c(name: String, inCone: Boolean, numCherries: Int, numWaffles: Int)

  property("Removing fields: IceCreamV1 migrate to IceCreamV2a") =
    forAll((iceCreamV1: IceCreamV1) => {
      val iceCreamV2a = iceCreamV1.migrateTo[IceCreamV2a]
      iceCreamV2a.name == iceCreamV1.name && iceCreamV2a.inCone == iceCreamV1.inCone
    })

  property("Reordering fields: IceCreamV1 migrate to IceCreamV2b") =
    forAll((iceCreamV1: IceCreamV1) => {
      val iceCreamV2b = iceCreamV1.migrateTo[IceCreamV2b]
      iceCreamV2b.name == iceCreamV1.name && iceCreamV2b.inCone == iceCreamV1.inCone &&
        iceCreamV2b.numCherries == iceCreamV1.numCherries
    })

  property("Adding new fields: IceCreamV1 migrate to IceCreamV2c") =
    forAll((iceCreamV1: IceCreamV1) => {
      val iceCreamV2c = iceCreamV1.migrateTo[IceCreamV2c]
      iceCreamV2c.name == iceCreamV1.name && iceCreamV2c.inCone == iceCreamV1.inCone &&
        iceCreamV2c.numCherries == iceCreamV1.numCherries && iceCreamV2c.numWaffles == 0
    })
