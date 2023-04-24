package com.peknight.generic.doobie.instances

import com.peknight.generic.doobie.ops.PutOps
import com.peknight.generic.mapper.Migration
import doobie.{Meta, Put}

trait PutInstances2:
  given putByte[A](using Migration[A, Byte]): Put[A] = PutOps.migrate[Byte, A](Meta[Byte].put)
  given putShort[A](using Migration[A, Short]): Put[A] = PutOps.migrate[Short, A](Meta[Short].put)
  given putInt[A](using Migration[A, Int]): Put[A] = PutOps.migrate[Int, A](Meta[Int].put)
  given putLong[A](using Migration[A, Long]): Put[A] = PutOps.migrate[Long, A](Meta[Long].put)
  given putFLoat[A](using Migration[A, Float]): Put[A] = PutOps.migrate[Float, A](Meta[Float].put)
  given putDouble[A](using Migration[A, Double]): Put[A] = PutOps.migrate[Double, A](Meta[Double].put)
  given putBigDecimal[A](using Migration[A, BigDecimal]): Put[A] = PutOps.migrate[BigDecimal, A](Meta[BigDecimal].put)
  given putBoolean[A](using Migration[A, Boolean]): Put[A] = PutOps.migrate[Boolean, A](Meta[Boolean].put)
  given putString[A](using Migration[A, String]): Put[A] = PutOps.migrate[String, A](Meta[String].put)
  given putByteArray[A](using Migration[A, Array[Byte]]): Put[A] = PutOps.migrate[Array[Byte], A](Meta[Array[Byte]].put)
end PutInstances2
