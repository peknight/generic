package com.peknight.generic.doobie.instances

import com.peknight.generic.doobie.ops.PutOps
import com.peknight.generic.mapper.Migration
import doobie.{Put, Meta}
import org.tpolecat.typename.TypeName

trait PutInstances extends PutInstances2:
  given tputByte[A](using Migration[A, Byte], TypeName[A]): Put[A] =
    PutOps.tmigrate[Byte, A](Meta[Byte].put)
  given tputShort[A](using Migration[A, Short], TypeName[A]): Put[A] =
    PutOps.tmigrate[Short, A](Meta[Short].put)
  given tputInt[A](using Migration[A, Int], TypeName[A]): Put[A] =
    PutOps.tmigrate[Int, A](Meta[Int].put)
  given tputLong[A](using Migration[A, Long], TypeName[A]): Put[A] =
    PutOps.tmigrate[Long, A](Meta[Long].put)
  given tputFLoat[A](using Migration[A, Float], TypeName[A]): Put[A] =
    PutOps.tmigrate[Float, A](Meta[Float].put)
  given tputDouble[A](using Migration[A, Double], TypeName[A]): Put[A] =
    PutOps.tmigrate[Double, A](Meta[Double].put)
  given tputBigDecimal[A](using Migration[A, BigDecimal], TypeName[A]): Put[A] =
    PutOps.tmigrate[BigDecimal, A](Meta[BigDecimal].put)
  given tputBoolean[A](using Migration[A, Boolean], TypeName[A]): Put[A] =
    PutOps.tmigrate[Boolean, A](Meta[Boolean].put)
  given tputString[A](using Migration[A, String], TypeName[A]): Put[A] =
    PutOps.tmigrate[String, A](Meta[String].put)
  given tputByteArray[A](using Migration[A, Array[Byte]], TypeName[A]): Put[A] =
    PutOps.tmigrate[Array[Byte], A](Meta[Array[Byte]].put)
end PutInstances
object PutInstances extends PutInstances
