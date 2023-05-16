package com.peknight.generic.doobie.instances

import com.peknight.generic.mapper.Migration
import doobie.{Meta, Put}
import org.tpolecat.typename.TypeName

trait PutInstances extends PutInstances2:
  given tputByte[A](using migration: Migration[A, Byte], typeName: TypeName[A]): Put[A] =
    Meta[Byte].put.tcontramap(migration.migrate)
  given tputShort[A](using migration: Migration[A, Short], typeName: TypeName[A]): Put[A] =
    Meta[Short].put.tcontramap(migration.migrate)
  given tputInt[A](using migration: Migration[A, Int], typeName: TypeName[A]): Put[A] =
    Meta[Int].put.tcontramap(migration.migrate)
  given tputLong[A](using migration: Migration[A, Long], typeName: TypeName[A]): Put[A] =
    Meta[Long].put.tcontramap(migration.migrate)
  given tputFLoat[A](using migration: Migration[A, Float], typeName: TypeName[A]): Put[A] =
    Meta[Float].put.tcontramap(migration.migrate)
  given tputDouble[A](using migration: Migration[A, Double], typeName: TypeName[A]): Put[A] =
    Meta[Double].put.tcontramap(migration.migrate)
  given tputBigDecimal[A](using migration: Migration[A, BigDecimal], typeName: TypeName[A]): Put[A] =
    Meta[BigDecimal].put.tcontramap(migration.migrate)
  given tputBoolean[A](using migration: Migration[A, Boolean], typeName: TypeName[A]): Put[A] =
    Meta[Boolean].put.tcontramap(migration.migrate)
  given tputString[A](using migration: Migration[A, String], typeName: TypeName[A]): Put[A] =
    Meta[String].put.tcontramap(migration.migrate)
  given tputByteArray[A](using migration: Migration[A, Array[Byte]], typeName: TypeName[A]): Put[A] =
    Meta[Array[Byte]].put.tcontramap(migration.migrate)
end PutInstances
object PutInstances extends PutInstances
