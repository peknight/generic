package com.peknight.generic.doobie.instances

import com.peknight.generic.doobie.ops.PutOps
import com.peknight.generic.mapper.Migration
import doobie.{Put, Meta}
import org.tpolecat.typename.TypeName

trait PutInstances extends PutInstances2:
  given putByte[A](using Migration[A, Byte], TypeName[A]): Put[A] =
    PutOps.tmigrate[Byte, A](Meta[Byte].put)
  given putShort[A](using Migration[A, Short], TypeName[A]): Put[A] =
    PutOps.tmigrate[Short, A](Meta[Short].put)
  given putInt[A](using Migration[A, Int], TypeName[A]): Put[A] =
    PutOps.tmigrate[Int, A](Meta[Int].put)
  given putLong[A](using Migration[A, Long], TypeName[A]): Put[A] =
    PutOps.tmigrate[Long, A](Meta[Long].put)
  given putFLoat[A](using Migration[A, Float], TypeName[A]): Put[A] =
    PutOps.tmigrate[Float, A](Meta[Float].put)
  given putDouble[A](using Migration[A, Double], TypeName[A]): Put[A] =
    PutOps.tmigrate[Double, A](Meta[Double].put)
  given putBigDecimal[A](using Migration[A, BigDecimal], TypeName[A]): Put[A] =
    PutOps.tmigrate[BigDecimal, A](Meta[BigDecimal].put)
  given putBoolean[A](using Migration[A, Boolean], TypeName[A]): Put[A] =
    PutOps.tmigrate[Boolean, A](Meta[Boolean].put)
  given putString[A](using Migration[A, String], TypeName[A]): Put[A] =
    PutOps.tmigrate[String, A](Meta[String].put)
  given putByteArray[A](using Migration[A, Array[Byte]], TypeName[A]): Put[A] =
    PutOps.tmigrate[Array[Byte], A](Meta[Array[Byte]].put)
end PutInstances
object PutInstances extends PutInstances
