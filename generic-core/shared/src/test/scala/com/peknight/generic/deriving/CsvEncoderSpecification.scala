package com.peknight.generic.deriving

import com.peknight.generic.deriving.Tree.{Branch, Leaf}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers.*

class CsvEncoderSpecification extends AnyFlatSpec:
  "CsvEncoder" should "have an instance for Int" in {
    CsvEncoder[Int].encode(0) shouldBe List("0")
  }

  it should "have an instance of IceCream" in {
    val iceCream = IceCream("Sundae", 1, false)
    CsvEncoder[IceCream].encode(iceCream) shouldBe List("Sundae", "1", "no")
  }

  it should "have an instance of Tree[Int]" in {
    val tree = Branch(Branch(Leaf(1), Branch(Leaf(2), Leaf(3))), Leaf(4))
    CsvEncoder[Tree[Int]].encode(tree) shouldBe List("1", "2", "3", "4")
  }
end CsvEncoderSpecification
