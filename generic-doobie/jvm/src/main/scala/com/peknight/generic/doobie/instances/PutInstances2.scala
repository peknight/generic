package com.peknight.generic.doobie.instances

import com.peknight.generic.mapper.Migration
import doobie.{Meta, Put}

trait PutInstances2:
  given putByte[A](using migration: Migration[A, Byte]): Put[A] =
    Meta[Byte].put.contramap(migration.migrate)
  given putShort[A](using migration: Migration[A, Short]): Put[A] =
    Meta[Short].put.contramap(migration.migrate)
  given putInt[A](using migration: Migration[A, Int]): Put[A] =
    Meta[Int].put.contramap(migration.migrate)
  given putLong[A](using migration: Migration[A, Long]): Put[A] =
    Meta[Long].put.contramap(migration.migrate)
  given putFLoat[A](using migration: Migration[A, Float]): Put[A] =
    Meta[Float].put.contramap(migration.migrate)
  given putDouble[A](using migration: Migration[A, Double]): Put[A] =
    Meta[Double].put.contramap(migration.migrate)
  given putBigDecimal[A](using migration: Migration[A, BigDecimal]): Put[A] =
    Meta[BigDecimal].put.contramap(migration.migrate)
  given putBoolean[A](using migration: Migration[A, Boolean]): Put[A] =
    Meta[Boolean].put.contramap(migration.migrate)
  given putString[A](using migration: Migration[A, String]): Put[A] =
    Meta[String].put.contramap(migration.migrate)
  given putByteArray[A](using migration: Migration[A, Array[Byte]]): Put[A] =
    Meta[Array[Byte]].put.contramap(migration.migrate)
end PutInstances2
