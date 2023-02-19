package com.peknight.generic.deriving

import com.peknight.generic.deriving.Tree
import com.peknight.generic.scalacheck.instances.all.given
import org.scalacheck.Prop.forAll
import org.scalacheck.{Arbitrary, Gen, Properties}

class CsvEncoderSpecification extends Properties("CsvEncoder"):

  property("should have an instance for IceCream") = forAll { (iceCream: IceCream) =>
    CsvEncoder[IceCream].encode(iceCream) == List(
      iceCream.name,
      s"${iceCream.numCherries}",
      if iceCream.inCone then "yes" else "no"
    )
  }

  given [A: Arbitrary]: Arbitrary[Tree[A]] = Arbitrary(Tree.gen(Arbitrary.arbitrary[A], 100))

  property("should have an instance for Tree") = forAll { (tree: Tree[Int]) =>
    CsvEncoder[Tree[Int]].encode(tree) == tree.toList.map(i => s"$i")
  }
end CsvEncoderSpecification
