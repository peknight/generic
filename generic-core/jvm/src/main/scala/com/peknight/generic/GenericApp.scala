package com.peknight.generic

import cats.data.State
import cats.{Applicative, Eval, Id, Traverse}
import com.peknight.generic.*
import com.peknight.generic.deriving.Generic
import com.peknight.generic.migration.{*, given}
import com.peknight.generic.ops.tuple.TupleOps.*

import scala.annotation.tailrec

object GenericApp extends App:

  case class IceCreamV1(name: String, numCherries: Int, inCone: Boolean)
  case class IceCreamV2a(name: String, inCone: Boolean)
  case class IceCreamV2b(name: String, inCone: Boolean, numCherries: Int)
  case class IceCreamV2c(name: String, inCone: Boolean, numCherries: Int, numWaffles: Int)

  println(IceCreamV1("Sundae", 1, true).migrateTo[IceCreamV2a])
  println(IceCreamV1("Sundae", 1, true).migrateTo[IceCreamV2b])
  println(IceCreamV1("Sundae", 1, true).migrateTo[IceCreamV2c])

  trait PrintString[A]:
    def print(a: A): String
  end PrintString

  given PrintString[String] with
    def print(a: String): String = a

  given PrintString[Int] with
    def print(a: Int): String = s"$a"

  given PrintString[Boolean] with
    def print(a: Boolean): String = s"$a"

  sealed trait Shape
  case class Circle(r: Int) extends Shape
  case class Square(r: Int) extends Shape

  given PrintString[Circle] with
    def print(a: Circle): String = s"$a"

  given PrintString[Square] with
    def print(a: Square): String = s"$a"

  val ins = summon[Generic[PrintString, IceCreamV1]]
  println(ins.labels)
  println(ins.instances)

  val ins2 = summon[Generic[PrintString, Shape]]
  println(ins2.labels)
  println(ins2.instances)
  println(ins2.ordinal(Circle(3)))
  println(ins2.ordinal(Square(3)))
  type StateInt[A] = State[Int, A]
  println(summon[Applicative[StateInt]])

  def state[A](a: A): StateInt[A] = State(s => (s + 1, a))
  println(sequence[StateInt, (Int, Boolean, String)]((state(1), state(false), state("abc"))).run(1).value)




