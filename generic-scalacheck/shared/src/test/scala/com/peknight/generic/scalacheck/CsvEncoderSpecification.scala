package com.peknight.generic.scalacheck

import com.peknight.generic.scalacheck.Tree.given
import com.peknight.generic.scalacheck.instances.all.given
import org.scalacheck.Prop.forAll
import org.scalacheck.Properties

class CsvEncoderSpecification extends Properties("CsvEncoder"):

  property("should have an instance for IceCream") = forAll { (iceCream: IceCream) =>
    CsvEncoder[IceCream].encode(iceCream) == List(
      iceCream.name,
      s"${iceCream.numCherries}",
      if iceCream.inCone then "yes" else "no"
    )
  }

  property("should have an instance for Tree") = forAll { (tree: Tree[Int]) =>
    CsvEncoder[Tree[Int]].encode(tree) == tree.toList.map(i => s"$i")
  }

end CsvEncoderSpecification
