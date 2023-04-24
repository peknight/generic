package com.peknight.generic.doobie.instances

import com.peknight.generic.doobie.ops.GetOps
import com.peknight.generic.mapper.Migration
import doobie.{Get, Meta}
import org.tpolecat.typename.TypeName

trait GetInstances extends GetInstances2:
  given tgetByte[A](using Migration[Byte, A], TypeName[A]): Get[A] =
    GetOps.tmigrate[Byte, A](Meta[Byte].get)
  given tgetShort[A](using Migration[Short, A], TypeName[A]): Get[A] =
    GetOps.tmigrate[Short, A](Meta[Short].get)
  given tgetInt[A](using Migration[Int, A], TypeName[A]): Get[A] =
    GetOps.tmigrate[Int, A](Meta[Int].get)
  given tgetLong[A](using Migration[Long, A], TypeName[A]): Get[A] =
    GetOps.tmigrate[Long, A](Meta[Long].get)
  given tgetFLoat[A](using Migration[Float, A], TypeName[A]): Get[A] =
    GetOps.tmigrate[Float, A](Meta[Float].get)
  given tgetDouble[A](using Migration[Double, A], TypeName[A]): Get[A] =
    GetOps.tmigrate[Double, A](Meta[Double].get)
  given tgetBigDecimal[A](using Migration[BigDecimal, A], TypeName[A]): Get[A] =
    GetOps.tmigrate[BigDecimal, A](Meta[BigDecimal].get)
  given tgetBoolean[A](using Migration[Boolean, A], TypeName[A]): Get[A] =
    GetOps.tmigrate[Boolean, A](Meta[Boolean].get)
  given tgetString[A](using Migration[String, A], TypeName[A]): Get[A] =
    GetOps.tmigrate[String, A](Meta[String].get)
  given tgetByteArray[A](using Migration[Array[Byte], A], TypeName[A]): Get[A] =
    GetOps.tmigrate[Array[Byte], A](Meta[Array[Byte]].get)
end GetInstances
object GetInstances extends GetInstances
