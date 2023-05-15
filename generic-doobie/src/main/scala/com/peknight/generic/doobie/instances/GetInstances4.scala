package com.peknight.generic.doobie.instances

import com.peknight.generic.mapper.Migration
import doobie.{Get, Meta}

trait GetInstances4:
  given getByte[A](using migration: Migration[Byte, A]): Get[A] =
    Meta[Byte].get.map(migration.migrate)
  given getShort[A](using migration: Migration[Short, A]): Get[A] =
    Meta[Short].get.map(migration.migrate)
  given getInt[A](using migration: Migration[Int, A]): Get[A] =
    Meta[Int].get.map(migration.migrate)
  given getLong[A](using migration: Migration[Long, A]): Get[A] =
    Meta[Long].get.map(migration.migrate)
  given getFLoat[A](using migration: Migration[Float, A]): Get[A] =
    Meta[Float].get.map(migration.migrate)
  given getDouble[A](using migration: Migration[Double, A]): Get[A] =
    Meta[Double].get.map(migration.migrate)
  given getBigDecimal[A](using migration: Migration[BigDecimal, A]): Get[A] =
    Meta[BigDecimal].get.map(migration.migrate)
  given getBoolean[A](using migration: Migration[Boolean, A]): Get[A] =
    Meta[Boolean].get.map(migration.migrate)
  given getString[A](using migration: Migration[String, A]): Get[A] =
    Meta[String].get.map(migration.migrate)
  given getByteArray[A](using migration: Migration[Array[Byte], A]): Get[A] =
    Meta[Array[Byte]].get.map(migration.migrate)
end GetInstances4

