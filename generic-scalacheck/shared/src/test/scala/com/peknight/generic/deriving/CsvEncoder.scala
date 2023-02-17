package com.peknight.generic.deriving

import com.peknight.generic.syntax.tuple.foldRight

import scala.deriving.Mirror as SMirror

trait CsvEncoder[A]:
  def encode(value: A): List[String]
end CsvEncoder
object CsvEncoder:
  def apply[A](using enc: CsvEncoder[A]): CsvEncoder[A] = enc
  def instance[A](func: A => List[String]): CsvEncoder[A] = func(_)

  // given productInstance[A](using generic: => Generic.Product[CsvEncoder, A]): CsvEncoder[A] with
  //   def encode(value: A): List[String] =
  //     generic.map[[_] =>> List[String]](value)([T] => (ft: CsvEncoder[T], t: T) => ft.encode(t))
  //       .foldRight(List.empty[String])([T] => (t: T, b: List[String]) => t.asInstanceOf[List[String]] ::: b)
  // end productInstance

  // given sumInstance[A](using generic: => Generic.Sum[CsvEncoder, A]): CsvEncoder[A] with
  //   def encode(value: A): List[String] = generic.instance(value).encode(value)
  // end sumInstance

  inline given derived[A](using generic: => Generic[CsvEncoder, A]): CsvEncoder[A] = generic.derive(
    gen ?=> instance(value => gen.map[[_] =>> List[String]](value)([T] => (ft: CsvEncoder[T], t: T) => ft.encode(t))
      .foldRight(List.empty[String])([T] => (t: T, b: List[String]) => t.asInstanceOf[List[String]] ::: b)),
    gen ?=> instance(value => gen.instance(value).encode(value))
  )

  given CsvEncoder[String] with
    def encode(value: String): List[String] = List(value)
  end given

  given CsvEncoder[Int] with
    def encode(value: Int): List[String] = List(s"$value")
  end given

  given CsvEncoder[Boolean] with
    def encode(value: Boolean): List[String] = List(if value then "yes" else "no")
  end given

end CsvEncoder
