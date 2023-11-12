package com.peknight.generic.tuple

import cats.syntax.option.*
import com.peknight.generic.scalacheck.IceCream
import com.peknight.generic.tuple.ops.{NonEmptyTupleOps, TupleOps}
import org.scalacheck.Prop.{forAll, propBoolean}
import org.scalacheck.{Prop, Properties}


class TupleOpsSpecification extends Properties("TupleOps"):

  given CanEqual[Option[IceCream], Option[IceCream]] = CanEqual.derived
  type T = (String, Int, Boolean)

  property("NonEmpty: (Some[String], Some[Int], Some[Boolean]).mapN(IceCream.apply) should produce Some[IceCream]") =
    forAll { (a: String, b: Int, c: Boolean) =>
      NonEmptyTupleOps.mapN[T, Option, IceCream]((a.some, b.some, c.some))(IceCream.apply) == IceCream(a, b, c).some
    }

  property("NonEmpty: (Some[String], None, Some[Boolean]).mapN(IceCream.apply) should produce None") = forAll {
    (a: String, c: Boolean) =>
      NonEmptyTupleOps.mapN[T, Option, IceCream]((a.some, none, c.some))(IceCream.apply) == none[IceCream]
  }

  property("NonEmpty: (None, None, None).mapN(IceCream.apply) should produce None") = Prop {
    NonEmptyTupleOps.mapN[T, Option, IceCream]((none, none, none))(IceCream.apply) == none[IceCream]
  }

  property("(Some[String], Some[Int], Some[Boolean]).mapN(IceCream.apply) should produce Some[IceCream]") =
    forAll { (a: String, b: Int, c: Boolean) =>
      TupleOps.mapN[T, Option, IceCream]((a.some, b.some, c.some))(IceCream.apply) == IceCream(a, b, c).some
    }

  property("(Some[String], None, Some[Boolean]).mapN(IceCream.apply) should produce None") = forAll {
    (a: String, c: Boolean) =>
      TupleOps.mapN[T, Option, IceCream]((a.some, none, c.some))(IceCream.apply) == none[IceCream]
  }

  property("(None, None, None).mapN(IceCream.apply) should produce None") = Prop {
    TupleOps.mapN[T, Option, IceCream]((none, none, none))(IceCream.apply) == none[IceCream]
  }

  property("EmptyTuple.mapN(identity) should produce Some[EmptyTuple]") = Prop {
    TupleOps.mapN[EmptyTuple, Option, EmptyTuple](EmptyTuple)(identity) == EmptyTuple.some
  }

  property("reverse") = forAll { (a: String, b: Int, c: Boolean) =>
    import com.peknight.generic.tuple.syntax.reverse
    (a, b, c).reverse == (c, b, a)
  }

  property("mkString(start, sep, end)") = forAll {
    (a: String, b: Int, c: Boolean, start: String, sep: String, end: String) =>
      import com.peknight.generic.tuple.syntax.mkString
      (a, b, c).mkString(start, sep, end) == s"$start$a$sep$b$sep$c$end"
  }

  property("mkString(sep)") = forAll { (a: String, b: Int, c: Boolean, sep: String) =>
    import com.peknight.generic.tuple.syntax.mkString
    (a, b, c).mkString(sep) == s"$a$sep$b$sep$c"
  }

  property("mkString") = forAll { (a: String, b: Int, c: Boolean) =>
    import com.peknight.generic.tuple.syntax.mkString
    (a, b, c).mkString == s"$a$b$c"
  }
