package com.peknight.generic.deriving

import com.peknight.generic.deriving.Generic.zip
import com.peknight.generic.syntax.tuple.foldRight

trait CsvEncoder[A]:
  def encode(value: A): List[String]
end CsvEncoder
object CsvEncoder:
  def apply[A](using enc: CsvEncoder[A]): CsvEncoder[A] = enc
  def instance[A](func: A => List[String]): CsvEncoder[A] = func(_)

  inline given derived[A <: Product](using generic: Generic[CsvEncoder, A]): CsvEncoder[A] = generic.derive(
    gen ?=> instance(a => gen.zip[[_] =>> List[String]](a)([T] => (ft: CsvEncoder[T], t: T) => ft.encode(t))
      .foldRight(List.empty[String])([T] => (t: T, b: List[String]) => t.asInstanceOf[List[String]] ::: b)),
    gen ?=> instance(a => gen.ordinal(a).encode(a))
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

  given CsvEncoder[Double] with
    def encode(value: Double): List[String] = List(s"$value")
  end given

  def writeCsv[A](values: List[A])(using enc: CsvEncoder[A]): String =
    values.map(value => enc.encode(value).mkString(",")).mkString("\n")
end CsvEncoder
