package com.peknight.generic.doobie.instances

import com.peknight.generic.mapper.Migration
import doobie.{Get, Meta}
import org.tpolecat.typename.TypeName

trait GetInstances3 extends GetInstances4:
  given tgetByte[A](using migration: Migration[Byte, A], typeName: TypeName[A]): Get[A] =
    Meta[Byte].get.tmap(migration.migrate)
  given tgetShort[A](using migration: Migration[Short, A], typeName: TypeName[A]): Get[A] =
    Meta[Short].get.tmap(migration.migrate)
  given tgetInt[A](using migration: Migration[Int, A], typeName: TypeName[A]): Get[A] =
    Meta[Int].get.tmap(migration.migrate)
  given tgetLong[A](using migration: Migration[Long, A], typeName: TypeName[A]): Get[A] =
    Meta[Long].get.tmap(migration.migrate)
  given tgetFLoat[A](using migration: Migration[Float, A], typeName: TypeName[A]): Get[A] =
    Meta[Float].get.tmap(migration.migrate)
  given tgetDouble[A](using migration: Migration[Double, A], typeName: TypeName[A]): Get[A] =
    Meta[Double].get.tmap(migration.migrate)
  given tgetBigDecimal[A](using migration: Migration[BigDecimal, A], typeName: TypeName[A]): Get[A] =
    Meta[BigDecimal].get.tmap(migration.migrate)
  given tgetBoolean[A](using migration: Migration[Boolean, A], typeName: TypeName[A]): Get[A] =
    Meta[Boolean].get.tmap(migration.migrate)
  given tgetString[A](using migration: Migration[String, A], typeName: TypeName[A]): Get[A] =
    Meta[String].get.tmap(migration.migrate)
  given tgetByteArray[A](using migration: Migration[Array[Byte], A], typeName: TypeName[A]): Get[A] =
    Meta[Array[Byte]].get.tmap(migration.migrate)
end GetInstances3
