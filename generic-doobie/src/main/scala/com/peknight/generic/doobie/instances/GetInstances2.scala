package com.peknight.generic.doobie.instances

import com.peknight.generic.doobie.ops.GetOps
import com.peknight.generic.mapper.Migration
import doobie.{Get, Meta}

trait GetInstances2:
  given getByte[A](using Migration[Byte, A]): Get[A] = GetOps.migrate[Byte, A](Meta[Byte].get)
  given getShort[A](using Migration[Short, A]): Get[A] = GetOps.migrate[Short, A](Meta[Short].get)
  given getInt[A](using Migration[Int, A]): Get[A] = GetOps.migrate[Int, A](Meta[Int].get)
  given getLong[A](using Migration[Long, A]): Get[A] = GetOps.migrate[Long, A](Meta[Long].get)
  given getFLoat[A](using Migration[Float, A]): Get[A] = GetOps.migrate[Float, A](Meta[Float].get)
  given getDouble[A](using Migration[Double, A]): Get[A] = GetOps.migrate[Double, A](Meta[Double].get)
  given getBigDecimal[A](using Migration[BigDecimal, A]): Get[A] = GetOps.migrate[BigDecimal, A](Meta[BigDecimal].get)
  given getBoolean[A](using Migration[Boolean, A]): Get[A] = GetOps.migrate[Boolean, A](Meta[Boolean].get)
  given getString[A](using Migration[String, A]): Get[A] = GetOps.migrate[String, A](Meta[String].get)
  given getByteArray[A](using Migration[Array[Byte], A]): Get[A] = GetOps.migrate[Array[Byte], A](Meta[Array[Byte]].get)
end GetInstances2

